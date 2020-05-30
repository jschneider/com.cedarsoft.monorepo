package com.cedarsoft.commons.javafx

import assertk.*
import assertk.assertions.*
import javafx.scene.paint.Color
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class FxUtilsKtTest {
  @Test
  fun test2RGB() {
    assertThat(Color.RED.toRGBCode()).isEqualTo("#FF0000")
    assertThat(Color.GREEN.toRGBCode()).isEqualTo("#008000")
    assertThat(Color.BLUE.toRGBCode()).isEqualTo("#0000FF")
    assertThat(Color.LIGHTGRAY.toRGBCode()).isEqualTo("#D3D3D3")
    assertThat(Color.DARKGRAY.toRGBCode()).isEqualTo("#A9A9A9")
    assertThat(Color.GRAY.toRGBCode()).isEqualTo("#808080")
  }
}
