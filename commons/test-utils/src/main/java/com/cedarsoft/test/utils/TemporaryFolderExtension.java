/*
 * MIT License
 * <p>
 * Copyright (c) 2017 Ralf Stuckert
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.cedarsoft.test.utils;

import org.junit.jupiter.api.extension.*;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class TemporaryFolderExtension implements ParameterResolver, AfterTestExecutionCallback, TestInstancePostProcessor {
  @Override
  public void afterTestExecution(TestExtensionContext context) throws Exception {
    // clean up test instance
    cleanUpTemporaryFolder(context);

    if (context.getParent().isPresent()) {
      // clean up injected member
      cleanUpTemporaryFolder(context.getParent().get());
    }
  }

  protected void cleanUpTemporaryFolder(ExtensionContext extensionContext) {
    for (TemporaryFolder temporaryFolder : getTemporaryFolders(extensionContext)) {
      temporaryFolder.after();
    }
  }

  @Override
  public boolean supports(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    Parameter parameter = parameterContext.getParameter();

    if (!(extensionContext instanceof TestExtensionContext)) {
      return false;
    }

    if (parameter.getType().isAssignableFrom(TemporaryFolder.class)) {
      return true;
    }

    if (!parameter.getType().isAssignableFrom(File.class)) {
      return false;
    }

    if (parameter.isAnnotationPresent(TempFolder.class)) {
      return true;
    }

    return parameter.isAnnotationPresent(TempFile.class);
  }

  @Override
  public Object resolve(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    TestExtensionContext testExtensionContext = (TestExtensionContext) extensionContext;
    try {
      TemporaryFolder temporaryFolder = createTemporaryFolder(testExtensionContext, testExtensionContext.getTestMethod().orElseThrow(() -> new IllegalStateException("No test method found")));

      Parameter parameter = parameterContext.getParameter();
      if (parameter.getType().isAssignableFrom(TemporaryFolder.class)) {
        return temporaryFolder;
      }
      if (parameter.isAnnotationPresent(TempFolder.class)) {
        return temporaryFolder.newFolder();
      }
      if (parameter.isAnnotationPresent(TempFile.class)) {
        TempFile annotation = parameter.getAnnotation(TempFile.class);
        if (!annotation.value().isEmpty()) {
          return temporaryFolder.newFile(annotation.value());
        }
        return temporaryFolder.newFile();
      }

      throw new ParameterResolutionException("unable to resolve parameter for " + parameterContext);
    } catch (IOException e) {
      throw new ParameterResolutionException("failed to create temp file or folder", e);
    }
  }

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
    for (Field field : testInstance.getClass().getDeclaredFields()) {
      if (field.getType().isAssignableFrom(TemporaryFolder.class)) {
        TemporaryFolder temporaryFolder = createTemporaryFolder(context, field);
        field.setAccessible(true);
        field.set(testInstance, temporaryFolder);
      }
    }
  }

  protected Iterable<TemporaryFolder> getTemporaryFolders(ExtensionContext extensionContext) {
    Map<Object, TemporaryFolder> map = getStore(extensionContext).get(extensionContext.getTestClass().get(), Map.class);
    if (map == null) {
      return Collections.emptySet();
    }
    return map.values();
  }

  protected TemporaryFolder createTemporaryFolder(ExtensionContext extensionContext, Member key) {
    Map<Member, TemporaryFolder> map = getStore(extensionContext).getOrComputeIfAbsent(extensionContext.getTestClass().get(), (c) -> new ConcurrentHashMap<>(), Map.class);
    return map.computeIfAbsent(key, member -> new TemporaryFolder());
  }

  @Nonnull
  protected ExtensionContext.Store getStore(@Nonnull ExtensionContext context) {
    return context.getStore(ExtensionContext.Namespace.create(getClass(), context));
  }
}