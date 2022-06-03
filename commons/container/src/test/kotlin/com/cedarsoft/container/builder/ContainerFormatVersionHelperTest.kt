package com.cedarsoft.container.builder

import com.cedarsoft.container.ContainerHelper
import com.cedarsoft.container.builder.ContainerFormatVersionHelper.addContainerVersionNumber
import com.cedarsoft.container.builder.ContainerFormatVersionHelper.verifyContainerVersionNumber
import com.cedarsoft.version.Version
import org.assertj.core.api.Java6Assertions.assertThat
import org.assertj.core.api.Java6Assertions.fail
import org.junit.jupiter.api.Test

/**
 */
class ContainerFormatVersionHelperTest {
  @Test
  fun testFailing() {
    testContainer {
      build { builder ->
        builder.addContainerVersionNumber(Version.of(7, 2, 3))
      }

      read { reader ->
        try {
          reader.verifyContainerVersionNumber()
          fail("Where is the exception?")
        } catch (e: UnsupportedContainerFormatVersionException) {
        }
      }
    }
  }

  @Test
  fun testBasic() {
    testContainer {
      build { builder ->
        builder.addContainerVersionNumber()
      }

      verify {
        assertThat(ContainerHelper.getZipToc(it.openStream())).isEqualTo("meta/version/1.0.0 (5 byte)\n")
        assertThat(ContainerHelper.getContentAsString(it.openStream(), "meta/version/1.0.0")).isEqualTo("0.0.1")
        assertThat(ContainerHelper.getContent(it.openStream(), "meta/version/1.0.0")).isEqualTo("0.0.1".toByteArray())
      }

      read { reader ->
        reader.verifyContainerVersionNumber()

        try {
          reader.nextEntry { descriptor, inputStream ->
            fail("Must not be called")
          }
          fail("Must not reach")
        } catch (e: Exception) {
        }
      }
    }
  }
}
