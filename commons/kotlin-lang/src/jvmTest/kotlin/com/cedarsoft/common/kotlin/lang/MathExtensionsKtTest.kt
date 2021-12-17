package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import kotlin.math.PI

class MathExtensionsKtTest {
  @Test
  fun testEven() {
    assertThat(1.0.isEven()).isFalse()
    assertThat(2.0.isEven()).isTrue()
    assertThat(3.0.isEven()).isFalse()
    assertThat(4.0.isEven()).isTrue()
    assertThat(4.00000000001.isEven()).isFalse()
    assertThat(0.0.isEven()).isTrue()

    assertThat((-1.0).isEven()).isFalse()
    assertThat((-2.0).isEven()).isTrue()
  }

  @Test
  fun testOdd() {
    assertThat(1.0.isOdd()).isTrue()
    assertThat(2.0.isOdd()).isFalse()
    assertThat(3.0.isOdd()).isTrue()
    assertThat(4.0.isOdd()).isFalse()
    assertThat(4.00000000001.isOdd()).isFalse()
    assertThat(0.0.isOdd()).isFalse()

    assertThat((-1.0).isOdd()).isTrue()
    assertThat((-2.0).isOdd()).isFalse()
  }

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

  @Test
  fun testPointIsLeftOfLine() {
    val line1 = Pair(Pair(0.0, 0.0), Pair(10.0, 10.0))
    val line2 = Pair(Pair(10.0, -10.0), Pair(-10.0, -10.0))
    val line3 = Pair(Pair(5.0, -10.0), Pair(-5.0, 10.0))

    val point1 = Pair(0.0, 5.0)
    val point2 = Pair(5.0, 0.0)
    val point3 = Pair(-50.0, 0.0)
    val point4 = Pair(0.0, 50.0)

    assertThat(pointIsLeftOfLine(line1.first.first, line1.first.second, line1.second.first, line1.second.second, point1.first, point1.second)).isEqualTo(true)
    assertThat(pointIsLeftOfLine(line1.first.first, line1.first.second, line1.second.first, line1.second.second, point2.first, point2.second)).isEqualTo(false)
    assertThat(pointIsLeftOfLine(line1.first.first, line1.first.second, line1.second.first, line1.second.second, point3.first, point3.second)).isEqualTo(true)
    assertThat(pointIsLeftOfLine(line1.first.first, line1.first.second, line1.second.first, line1.second.second, point4.first, point4.second)).isEqualTo(true)

    assertThat(pointIsLeftOfLine(line2.first.first, line2.first.second, line2.second.first, line2.second.second, point1.first, point1.second)).isEqualTo(false)
    assertThat(pointIsLeftOfLine(line2.first.first, line2.first.second, line2.second.first, line2.second.second, point2.first, point2.second)).isEqualTo(false)
    assertThat(pointIsLeftOfLine(line2.first.first, line2.first.second, line2.second.first, line2.second.second, point3.first, point3.second)).isEqualTo(false)
    assertThat(pointIsLeftOfLine(line2.first.first, line2.first.second, line2.second.first, line2.second.second, point4.first, point4.second)).isEqualTo(false)

    assertThat(pointIsLeftOfLine(line3.first.first, line3.first.second, line3.second.first, line3.second.second, point1.first, point1.second)).isEqualTo(false)
    assertThat(pointIsLeftOfLine(line3.first.first, line3.first.second, line3.second.first, line3.second.second, point2.first, point2.second)).isEqualTo(false)
    assertThat(pointIsLeftOfLine(line3.first.first, line3.first.second, line3.second.first, line3.second.second, point3.first, point3.second)).isEqualTo(true)
    assertThat(pointIsLeftOfLine(line3.first.first, line3.first.second, line3.second.first, line3.second.second, point4.first, point4.second)).isEqualTo(false)
  }
}
