package com.cedarsoft.io

import com.cedarsoft.test.utils.TempFolder
import com.cedarsoft.test.utils.WithTempFiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

/**
 */
@WithTempFiles
internal class FileUtilsTest {
  @Test
  internal fun testInlineFunction(@TempFolder folder: File) {
    assertThat(folder).isDirectory().exists()
    assertThat(folder.createDirectoryIfNotExisting()).isSameAs(folder)
  }
}
