package it.neckar.open.test.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

/**
 */
@WithTempFiles
internal class TemporaryFolderExtensionTest {
  @Test
  fun testTmpFolder(@TempFolder folder: File?) {
    assertThat(folder).isNotNull.exists().isDirectory
  }

  @Test
  fun testTmpFile(@TempFile file: File?) {
    assertThat(file).isNotNull.exists().isFile
  }

  @Test
  fun testTempFolder(tmp: TemporaryFolder) {
    assertThat(tmp.newFolder()).exists().isDirectory
  }
}
