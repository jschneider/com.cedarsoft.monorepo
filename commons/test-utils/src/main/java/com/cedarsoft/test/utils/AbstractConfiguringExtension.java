package com.cedarsoft.test.utils;

import org.junit.jupiter.api.extension.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * Abstract base class for extensions that configure stuff and revert it after the tests
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractConfiguringExtension<T, A extends Annotation> implements BeforeEachCallback, AfterEachCallback, BeforeAllCallback, AfterAllCallback {
  @Nonnull
  private final Class<T> storedObjectType;
  @Nonnull
  private final Class<A> enumType;
  @Nonnull
  private final String key;

  protected AbstractConfiguringExtension(@Nonnull Class<T> storedObjectType, @Nonnull Class<A> enumType, @Nonnull String key) {
    this.storedObjectType = storedObjectType;
    this.key = key;
    this.enumType = enumType;
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    getConfiguredType(extensionContext).ifPresent(value -> before(value, extensionContext, Scope.CLASS));
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) throws Exception {
    after(extensionContext, Scope.CLASS);
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    getConfiguredType(extensionContext).ifPresent(value -> before(value, extensionContext, Scope.METHOD));
  }

  @Override
  public void afterEach(ExtensionContext extensionContext) throws Exception {
    after(extensionContext, Scope.METHOD);
  }

  @Nonnull
  protected Optional<T> getConfiguredType(@Nonnull ExtensionContext context) {
    return context.getElement()
      .flatMap(annotatedElement -> Optional.ofNullable(annotatedElement.getAnnotation(enumType)))
      .flatMap(this::convert);
  }

  @Nonnull
  private String createStoreKey(@Nonnull Scope scope) {
    return scope.name() + "." + key;
  }

  private void before(@Nonnull T value, @Nonnull ExtensionContext context, @Nonnull Scope scope) {
    context.getStore(ExtensionContext.Namespace.GLOBAL).put(createStoreKey(scope), getOldValue());
    applyValue(value);
  }

  private void after(@Nonnull ExtensionContext context, @Nonnull Scope scope) {
    @Nullable T originalValue = context.getStore(ExtensionContext.Namespace.GLOBAL).get(createStoreKey(scope), storedObjectType);
    if (originalValue != null) {
      applyValue(originalValue);
    }
  }

  /**
   * Extracts the type from the annotation
   */
  @Nonnull
  protected abstract Optional<T> convert(@Nonnull A annotation);

  /**
   * Returns the old value that has been set originally
   */
  @Nonnull
  protected abstract T getOldValue();

  /**
   * Applies the value. This method is called twice. Once before the test is run with the new value. Once after the test has run with the old value
   */
  protected abstract void applyValue(@Nonnull T value);

  /**
   * The scope for the store
   */
  protected enum Scope {
    CLASS,
    METHOD,
  }

}

