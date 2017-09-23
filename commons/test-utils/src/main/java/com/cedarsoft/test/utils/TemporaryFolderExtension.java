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
import java.lang.reflect.Parameter;

/**
 * Extension that fills a File parameter with a temporary file or folder.
 * Use {@link WithTempFiles} at the class/method and add {@link TempFolder} oder {@link TempFile} to the test method parameters
 */
public class TemporaryFolderExtension extends AbstractResourceProvidingExtension<TemporaryFolder> {
  protected TemporaryFolderExtension() {
    super(TemporaryFolder.class);
  }

  @Nonnull
  @Override
  protected TemporaryFolder createResource(ExtensionContext extensionContext) {
    return new TemporaryFolder();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    if (super.supportsParameter(parameterContext, extensionContext)) {
      return true;
    }

    if (!parameterContext.getParameter().getType().isAssignableFrom(File.class)) {
      return false;
    }

    if (parameterContext.getParameter().isAnnotationPresent(TempFolder.class)) {
      return true;
    }

    return parameterContext.getParameter().isAnnotationPresent(TempFile.class);
  }

  @Nonnull
  @Override
  protected Object convertResourceForParameter(@Nonnull Parameter parameter, @Nonnull TemporaryFolder resource) throws ParameterResolutionException, IOException {
    if (parameter.isAnnotationPresent(TempFolder.class)) {
      return resource.newFolder();
    }
    if (parameter.isAnnotationPresent(TempFile.class)) {
      TempFile annotation = parameter.getAnnotation(TempFile.class);
      if (!annotation.value().isEmpty()) {
        return resource.newFile(annotation.value());
      }
      return resource.newFile();
    }

    throw new ParameterResolutionException("unable to resolve parameter for " + parameter);
  }

  @Override
  protected void cleanup(@Nonnull TemporaryFolder resource) {
    resource.delete();
  }
}