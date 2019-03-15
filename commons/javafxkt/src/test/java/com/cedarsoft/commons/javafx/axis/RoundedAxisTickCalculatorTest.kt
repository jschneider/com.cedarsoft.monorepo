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
    assertThat(RoundedAxisTickCalculator(0.0, 10.0).calculateTickUnit()).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(0.0, 11.0).calculateTickUnit()).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(0.0, 9.0).calculateTickUnit()).isEqualTo(1.0)
    assertThat(RoundedAxisTickCalculator(0.0, 724.0).calculateTickUnit()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 999.0).calculateTickUnit()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1001.0).calculateTickUnit()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1999.0).calculateTickUnit()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2000.0).calculateTickUnit()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2001.0).calculateTickUnit()).isEqualTo(100.0)
  }

  @Test
  internal fun calculateFirstTick() {
    assertThat(RoundedAxisTickCalculator(0.0, 10.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 11.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 9.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 724.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 999.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1001.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 1999.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2000.0).calculateFirstTick()).isEqualTo(0.0)
    assertThat(RoundedAxisTickCalculator(0.0, 2001.0).calculateFirstTick()).isEqualTo(0.0)

    assertThat(RoundedAxisTickCalculator(9.0, 100.0).calculateFirstTick()).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(10.0, 100.0).calculateFirstTick()).isEqualTo(10.0)
    assertThat(RoundedAxisTickCalculator(11.0, 100.0).calculateFirstTick()).isEqualTo(20.0)
    assertThat(RoundedAxisTickCalculator(11.0, 1000.0).calculateFirstTick()).isEqualTo(100.0)
    assertThat(RoundedAxisTickCalculator(11.0, 10000.0).calculateFirstTick()).isEqualTo(1000.0)
  }

  @Test
  internal fun testIt() {
    assertThat(RoundedAxisTickCalculator(0.0, 10.0).calculateTickValues()).containsExactly(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)
    assertThat(RoundedAxisTickCalculator(-0.5, 10.0).calculateTickValues()).containsExactly(-0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)
    assertThat(RoundedAxisTickCalculator(0.5, 10.0).calculateTickValues()).containsExactly(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0)
  }
}
