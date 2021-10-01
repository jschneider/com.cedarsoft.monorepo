package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import kotlin.math.PI

class MathExtensionsKtTest {
  @Test
  fun testMaxNullable() {
    assertThat(1.0.coerceAtLeastOrNull(12.0)).isEqualTo(12.0)
    assertThat(1.0.coerceAtLeastOrNull(null)).isEqualTo(1.0)
    assertThat((null as Double?).coerceAtLeastOrNull(12.0)).isEqualTo(12.0)
    assertThat((null as Double?).coerceAtLeastOrNull(1.0)).isEqualTo(1.0)
    assertThat((null as Double?).coerceAtLeastOrNull(null)).isEqualTo(null)
  }

  @Test
  fun ceilTo10() {
    assertThat(12.0.roundDecimalPlaces(-1)).isEqualTo(10.0)
    assertThat(20.0.ceilDecimalPlaces(-1)).isEqualTo(20.0)
    assertThat(99.0.roundDecimalPlaces(-1)).isEqualTo(100.0)
  }

  @Test
  fun testPercent() {
    assertThat(4.0.percent).isEqualTo(0.04)
    assertThat(4.01.percent).isEqualTo(0.0401)
    assertThat(4.percent).isEqualTo(0.04)
  }

  @Test
  fun testBetween() {
    assertThat(7.0.betweenInclusive(1.0, 7.0)).isTrue()
    assertThat(7.0.betweenInclusive(1.0, 7.1)).isTrue()
    assertThat(7.0.betweenInclusive(1.0, 6.999)).isFalse()
  }

  @Test
  fun testBetweenOtherOrder() {
    assertThat(7.0.betweenInclusive(7.0, 1.0)).isTrue()
    assertThat(7.0.betweenInclusive(7.1, 1.0)).isTrue()
    assertThat(7.0.betweenInclusive(6.999, 1.0)).isFalse()
  }

  @Test
  fun testToRadians() {
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
