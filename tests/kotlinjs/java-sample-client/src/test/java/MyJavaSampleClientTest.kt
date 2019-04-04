package com.cedarsoft.tests.kotlinjs.java

import com.cedarsoft.tests.kotlinjs.lib.getTheTime
import com.cedarsoft.tests.kotlinjs.lib.helloSampleWorld
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class MyJavaSampleClientTest {
  @Test
  internal fun testIt() {
    val result = helloSampleWorld()
    assertThat(result).isEqualTo(42)
  }

  @Test
  internal fun testTime() {
    assertThat(getTheTime()).startsWith("Time: 155")
  }
}
