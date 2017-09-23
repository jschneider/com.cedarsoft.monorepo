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


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

/**
 * Represents a temporary folder
 */
public class TemporaryFolder {
  @Nullable
  private File root;

  protected File getRoot() throws IOException {
    if (root == null) {
      root = createTemporaryFolder(null);
    }
    return root;
  }

  public void delete() {
    if (root != null) {
      recursiveDelete(root);
      root = null;
    }
  }

  @Nonnull
  public File newFile(@Nonnull String fileName) throws IOException {
    File file = new File(getRoot(), fileName);
    if (!file.createNewFile()) {
      throw new IOException(String.format("failed to create file %s in folder %s", fileName, getRoot()));
    }
    return file;
  }

  @Nonnull
  public File newFile() throws IOException {
    return File.createTempFile("junit", null, getRoot());
  }

  @Nonnull
  public File newFolder() throws IOException {
    return createTemporaryFolder(getRoot());
  }

  @Nonnull
  private static File createTemporaryFolder(@Nullable File base) throws IOException {
    File createdFolder = File.createTempFile("junit", "", base);
    createdFolder.delete();
    createdFolder.mkdir();
    return createdFolder;
  }

  private static void recursiveDelete(@Nonnull File file) {
    File[] files = file.listFiles();
    if (files != null) {
      for (File each : files) {
        recursiveDelete(each);
      }
    }

    if (!file.delete()) {
      file.deleteOnExit();
    }
  }
}