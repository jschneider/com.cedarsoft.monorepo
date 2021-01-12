package com.cedarsoft.common.kotlin.lang

import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import kotlin.math.PI

internal class MathExtensionsKtTest {
  @Test
  internal fun testToRadians() {
    AssertionsForClassTypes.assertThat(0.0.toRadians()).isEqualTo(0.0)
    AssertionsForClassTypes.assertThat(45.0.toRadians()).isEqualTo(PI / 4.0 * 1) //bottom-right
    AssertionsForClassTypes.assertThat(90.0.toRadians()).isEqualTo(PI / 4.0 * 2) //bottom
    AssertionsForClassTypes.assertThat(135.0.toRadians()).isEqualTo(PI / 4.0 * 3) //bottom-left
    AssertionsForClassTypes.assertThat(180.0.toRadians()).isEqualTo(PI / 4.0 * 4) //left
    AssertionsForClassTypes.assertThat(225.0.toRadians()).isEqualTo(PI / 4.0 * 5) //top-left
    AssertionsForClassTypes.assertThat(270.0.toRadians()).isEqualTo(PI / 4.0 * 6) //top
    AssertionsForClassTypes.assertThat(315.0.toRadians()).isEqualTo(PI / 4.0 * 7) //top right

    //More than one rotation
    AssertionsForClassTypes.assertThat(360.0.toRadians()).isEqualTo(PI / 4.0 * 8) //top right
    AssertionsForClassTypes.assertThat(405.0.toRadians()).isEqualTo(PI / 4.0 * 9) //top right

    //Negative values
    AssertionsForClassTypes.assertThat(-45.0.toRadians()).isEqualTo(PI / 4.0 * -1) //bottom-right
    AssertionsForClassTypes.assertThat(-90.0.toRadians()).isEqualTo(PI / 4.0 * -2) //bottom-right
  }

  @Test
  internal fun testGeneric() {
    for (i in -50..100) {
      val degrees = 45 * i
      val expectedRad = PI / 4.0 * i

      val toRadians = degrees.toRadians()

      AssertionsForClassTypes.assertThat(toRadians).describedAs("$degrees°").isEqualTo(expectedRad)
    }
  }
}
