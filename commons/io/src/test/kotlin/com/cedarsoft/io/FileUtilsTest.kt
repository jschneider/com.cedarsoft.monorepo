package com.cedarsoft.io

import org.assertj.core.api.Assertions
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.junit.rules.TemporaryFolder

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class FileUtilsTest {
  @Test
  internal fun testInlineFunction() {
    val folder = FileUtilsTest.companion.tmp.newFolder()
    Assertions.assertThat(folder).isFile().exists()
  }

  object companion {
    @JvmStatic
    @Rule
    var tmp = TemporaryFolder()
  }
}