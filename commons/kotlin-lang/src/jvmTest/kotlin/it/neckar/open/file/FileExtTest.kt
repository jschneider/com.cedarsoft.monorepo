package it.neckar.open.file

import assertk.*
import assertk.assertions.*
import it.neckar.open.test.utils.OnlyLinux
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
