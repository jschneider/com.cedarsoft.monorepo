package it.neckar.open.javafx

import assertk.*
import assertk.assertions.*
import javafx.scene.paint.Color
import org.junit.jupiter.api.Test

/**
 */
class FxUtilsKtTest {
  @Test
  fun test2RGB() {
    assertThat(Color.RED.toRGBHex()).isEqualTo("#FF0000")
    assertThat(Color.GREEN.toRGBHex()).isEqualTo("#008000")
    assertThat(Color.BLUE.toRGBHex()).isEqualTo("#0000FF")
    assertThat(Color.LIGHTGRAY.toRGBHex()).isEqualTo("#D3D3D3")
    assertThat(Color.DARKGRAY.toRGBHex()).isEqualTo("#A9A9A9")
    assertThat(Color.GRAY.toRGBHex()).isEqualTo("#808080")
  }
}
