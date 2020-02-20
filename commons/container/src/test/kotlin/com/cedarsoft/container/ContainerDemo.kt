package com.cedarsoft.container

import com.cedarsoft.container.builder.ZippedContainerBuilder
import com.cedarsoft.container.builder.ZippedContainerReader
import com.cedarsoft.version.Version
import com.google.common.io.ByteStreams
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class ContainerDemo {
  @Test
  internal fun testIt() {
    val out = ByteArrayOutputStream().use {
      ZippedContainerBuilder(it).use { containerBuilder ->
        containerBuilder.addEntry(ContainerEntryDescriptor("data/test", Version.of(1, 2, 3))) {
          it.write("daContent".toByteArray())
        }
      }
      it
    }

    File("/tmp/a.zip").outputStream().write(out.toByteArray())

    assertThat(ContainerHelper.getZipToc(out.toByteArray().inputStream())).isEqualTo("data/test/1.2.3 (9 byte)\n")

    //Read the container
    out.toByteArray().inputStream().use {
      val reader = ZippedContainerReader(it)

      reader.nextEntry { descriptor, inputStream ->
        assertThat(descriptor.id).isEqualTo("data/test")
        assertThat(descriptor.version).isEqualTo(Version.of(1, 2, 3))

        val content = ByteStreams.toByteArray(inputStream).toString(Charsets.UTF_8)
        assertThat(content).isEqualTo("daContent")
      }
    }
  }
}
