package com.cedarsoft.common.file

import assertk.*
import assertk.assertions.*
import com.cedarsoft.test.utils.OnlyLinux
import org.junit.jupiter.api.Test
import java.io.File

class FileExtTest {
  @OnlyLinux
  @Test
  fun testIt() {
    val path = "~/tmp"
    val file = File(path)

    assertThat(file.replaceLeadingTilde().absolutePath).isEqualTo(System.getProperty("user.home") + "/tmp")
  }
}
