package com.cedarsoft.test.utils;

import java.io.File;

import javax.annotation.Nonnull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@WithTempFiles
class TemporaryFolderExtensionTest {
  @Test
  void testTmpFolder(@Nonnull @TempFolder File folder) {
    Assertions.assertThat(folder).isNotNull().exists().isDirectory();
  }

  @Test
  void testTmpFile(@Nonnull @TempFile File file) {
    Assertions.assertThat(file).isNotNull().exists().isFile();
  }

  @Test
  void testTempFolder(@Nonnull TemporaryFolder tmp) throws Exception {
    Assertions.assertThat(tmp.newFolder()).exists().isDirectory();
  }
}
