package com.cedarsoft.io

import com.cedarsoft.test.utils.TempFolder
import com.cedarsoft.test.utils.WithTempFiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

/**
 */
@WithTempFiles
class FileUtilsTest {
  @Test
  fun testInlineFunction(@TempFolder folder: File) {
    assertThat(folder).isDirectory().exists()
    assertThat(folder.createDirectoryIfNotExisting()).isSameAs(folder)
  }

  @Test
  fun testWriteWithRename(@TempFolder folder: File) {
    val file = File(folder, "myFile")
    file.writeTextWithRename("Content")
    assertThat(file).hasContent("Content")
  }
}



