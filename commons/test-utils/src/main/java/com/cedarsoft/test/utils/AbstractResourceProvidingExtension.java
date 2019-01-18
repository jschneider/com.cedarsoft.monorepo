package com.cedarsoft.test.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.extension.*;

/**
 * Abstract base class for extensions that provide a resource
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractResourceProvidingExtension<T> implements ParameterResolver, AfterTestExecutionCallback, TestInstancePostProcessor {
  @Nonnull
  private final Class<T> resourceType;

  protected AbstractResourceProvidingExtension(@Nonnull Class<T> resourceType) {
    this.resourceType = resourceType;
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
    for (Field field : testInstance.getClass().getDeclaredFields()) {
      if (resourceType.isAssignableFrom(field.getType())) {
        T resource = getResource(context, field);
        field.setAccessible(true);
        field.set(testInstance, resource);
      }
    }
  }

  /**
   * Creates a resource
   */
  @Nonnull
  protected T getResource(ExtensionContext extensionContext, Member key) {
    //noinspection unchecked
    Map<Member, T> map = getStore(extensionContext).getOrComputeIfAbsent(extensionContext.getTestClass().get(), (c) -> new ConcurrentHashMap<>(), Map.class);
    return map.computeIfAbsent(key, member -> createResource(extensionContext));
  }

  /**
   * Creates the resource
   */
  @Nonnull
  protected abstract T createResource(ExtensionContext extensionContext);

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    try {
      T resource = getResource(extensionContext, extensionContext.getTestMethod().orElseThrow(() -> new IllegalStateException("No test method found")));

      Parameter parameter = parameterContext.getParameter();
      if (resourceType.isAssignableFrom(parameter.getType())) {
        //return the resource directly
        return resource;
      }

      return convertResourceForParameter(parameter, resource);
    } catch (Exception e) {
      throw new ParameterResolutionException("failed to create resource", e);
    }
  }

  /**
   * Converts the given resource to an object for the parameter - based on the type and annotations of the parameter
   */
  @Nonnull
  protected abstract Object convertResourceForParameter(@Nonnull Parameter parameter, @Nonnull T resource) throws ParameterResolutionException, Exception;


  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    // clean up test instance
    cleanupResources(context);

    if (context.getParent().isPresent()) {
      // clean up injected member
      cleanupResources(context.getParent().get());
    }
  }

  protected void cleanupResources(@Nonnull ExtensionContext extensionContext) {
    for (T resource : getResources(extensionContext)) {
      cleanup(resource);
    }
  }

  /**
   * Callback to cleanup the given resource
   */
  protected abstract void cleanup(@Nonnull T resource);


  @Nonnull
  protected Iterable<T> getResources(ExtensionContext extensionContext) {
    //noinspection unchecked
    Map<Object, T> map = getStore(extensionContext).get(extensionContext.getTestClass().get(), Map.class);
    if (map == null) {
      return Collections.emptySet();
    }
    return map.values();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return resourceType.isAssignableFrom(parameterContext.getParameter().getType());
  }


  /**
   * Returns the store for this class and context
   */
  @Nonnull
  protected ExtensionContext.Store getStore(@Nonnull ExtensionContext context) {
    return context.getStore(ExtensionContext.Namespace.create(getClass(), context));
  }

}
