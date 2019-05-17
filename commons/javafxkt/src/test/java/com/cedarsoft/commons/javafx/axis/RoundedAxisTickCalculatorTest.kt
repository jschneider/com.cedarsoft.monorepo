package com.cedarsoft.commons.javafx.axis

import com.cedarsoft.javafx.test.JavaFxTest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@JavaFxTest
internal class RoundedAxisTickCalculatorTest {
  @Test
  internal fun calculateTickUnit() {
    assertThat(RoundedAxisTickCalculator(0.0, 10.0).calculateTickDistance()).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(0.0, 11.0).calculateTickDistance()).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(0.0, 9.0).calculateTickDistance()).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(0.0, 724.0).calculateTickDistance()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 999.0).calculateTickDistance()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1001.0).calculateTickDistance()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1999.0).calculateTickDistance()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2000.0).calculateTickDistance()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2001.0).calculateTickDistance()).isEqualTo(100.0)
  }

  @Test
  internal fun testTickUnit() {
    assertThat(RoundedAxisTickCalculator(0.0, 10.0).calculateTickDistance(10)).isEqualTo(5.0)
    assertThat(RoundedAxisTickCalculator(0.0, 10.0).calculateTickDistance(11)).isEqualTo(1.0)

    assertThat(RoundedAxisTickCalculator(0.0, 9.0).calculateTickDistance(10)).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(0.0, 11.0).calculateTickDistance(10)).isEqualTo(5.0)

    assertThat(RoundedAxisTickCalculator(1.0, 10.0).calculateTickDistance(10)).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(1.0, 9.0).calculateTickDistance(10)).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(1.0, 11.0).calculateTickDistance(10)).isEqualTo(5.0)


    assertThat(RoundedAxisTickCalculator(0.0, 100.0).calculateTickDistance(10)).isEqualTo(50.0)
    assertThat(RoundedAxisTickCalculator(0.0, 101.0).calculateTickDistance(10)).isEqualTo(50.0)
    assertThat(RoundedAxisTickCalculator(0.0, 99.0).calculateTickDistance(10)).isEqualTo(10.0)

    assertThat(RoundedAxisTickCalculator(1.0, 100.0).calculateTickDistance(10)).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(1.0, 101.0).calculateTickDistance(10)).isEqualTo(50.0)
    assertThat(RoundedAxisTickCalculator(1.0, 99.0).calculateTickDistance(10)).isEqualTo(10.0)

    assertThat(RoundedAxisTickCalculator(10.0, 100.0).calculateTickDistance(10)).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(10.0, 101.0).calculateTickDistance(10)).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(10.0, 99.0).calculateTickDistance(10)).isEqualTo(10.0)

    assertThat(RoundedAxisTickCalculator(40.0, 100.0).calculateTickDistance(10)).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(50.0, 101.0).calculateTickDistance(10)).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(60.0, 99.0).calculateTickDistance(10)).isEqualTo(5.0)

    assertThat(RoundedAxisTickCalculator(70.0, 100.0).calculateTickDistance(10)).isEqualTo(5.0)
    assertThat(RoundedAxisTickCalculator(80.0, 101.0).calculateTickDistance(10)).isEqualTo(5.0)
    assertThat(RoundedAxisTickCalculator(90.0, 99.0).calculateTickDistance(10)).isEqualTo(1.0)
  }

  @Test
  internal fun calculateFirstTick() {
    assertThat(RoundedAxisTickCalculator(0.0, 10.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 11.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 9.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 724.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 999.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1001.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1999.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2000.0).calculateTickBase()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2001.0).calculateTickBase()).isEqualTo(0.0)

    assertThat(RoundedAxisTickCalculator(9.0, 100.0).calculateTickBase()).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(10.0, 100.0).calculateTickBase()).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(11.0, 100.0).calculateTickBase()).isEqualTo(20.0)
    assertThat(RoundedAxisTickCalculator(11.0, 1000.0).calculateTickBase()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(11.0, 10000.0).calculateTickBase()).isEqualTo(1000.0)
  }

  @Test
  internal fun testUpperValue() {
    assertThat(RoundedAxisTickCalculator(0.0, 11.0).calculateTickValues(15)).containsExactly(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0)
    assertThat(RoundedAxisTickCalculator(-0.5, 11.0).calculateTickValues(15)).containsExactly(-0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0)
    assertThat(RoundedAxisTickCalculator(0.5, 10.5).calculateTickValues(15)).containsExactly(0.5, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.5)
  }
}
