package com.cedarsoft.io

import com.cedarsoft.test.utils.TempFolder
import com.cedarsoft.test.utils.WithTempFiles
import com.cedarsoft.test.utils.findChildRecursively
import com.cedarsoft.test.utils.getAllChildrenNames
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
  @Test
  fun testDirRename(@TempFolder baseDirectory: File) {
    val dirName = "dirName"
    val sourceTop = File(baseDirectory, "$dirName.new") // new file
    sourceTop.mkdir()
    val sourceMiddle = File(sourceTop, "middle")
    sourceMiddle.mkdir()
    val sourceLow = File(sourceMiddle, "low")
    sourceLow.createNewFile()
    sourceLow.writeText("ContentSource")

    val targetTop = File(baseDirectory, dirName) //to be overwritten
    targetTop.mkdir()
    val targetMiddle = File(targetTop, "middle")
    targetMiddle.mkdir()
    val targetMiddle2 = File(targetTop, "middle2") // different structure to new file
    targetMiddle2.mkdir()

    val backupFile = File(baseDirectory, "$dirName.old")
    targetTop.renameTo(backupFile)
    sourceTop.renameTo(targetTop)

    backupFile.deleteRecursively()
    sourceTop.deleteRecursively()
    assertThat(targetTop.getAllChildrenNames()).contains("low")
    assertThat(targetTop.getAllChildrenNames()).doesNotContain("middle2")
    assertThat(targetTop.findChildRecursively("low")?.readText()).isEqualTo("ContentSource")
  }

}



