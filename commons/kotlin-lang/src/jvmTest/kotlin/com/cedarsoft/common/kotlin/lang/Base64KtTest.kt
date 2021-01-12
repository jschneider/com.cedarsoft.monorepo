package com.cedarsoft.common.kotlin.lang

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class Base64KtTest {
  @Test
  fun testIt2() {
    val raw = "$\"+~"

    assertThat(raw.toBase64()).isEqualTo("JCIrfg==")
    assertThat(raw.toBase64().fromBase64().decodeToString()).isEqualTo(raw)
  }
}
