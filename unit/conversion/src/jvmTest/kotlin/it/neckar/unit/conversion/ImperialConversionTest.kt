package it.neckar.unit.conversion

import assertk.*
import assertk.assertions.*
import com.cedarsoft.unit.si.degC
import com.cedarsoft.unit.si.degF
import org.junit.jupiter.api.Test

class ImperialConversionTest {
  @Test
  fun testTempConversion() {
    testTemperatureRoundtrip(5600.0, 10112.0)
    testTemperatureRoundtrip(100.0, 212.0)
    testTemperatureRoundtrip(30.0, 86.0)
    testTemperatureRoundtrip(0.0, 32.0)
  }

  private fun testTemperatureRoundtrip(celsius: @degC Double, fahrenheit: @degF Double) {
    assertThat(ImperialConversion.celsius2fahrenheit(celsius), "converting $celsius °C").isEqualTo(fahrenheit)
    assertThat(ImperialConversion.fahrenheit2celsius(fahrenheit), "converting $fahrenheit °F").isEqualTo(celsius)
  }

  @Test
  fun testUsGAl() {
    assertThat(ImperialConversion.litre2usGallon(1.0)).isCloseTo(0.26417205, 0.0001)
    assertThat(ImperialConversion.usGallon2litre(17.0)).isCloseTo(64.3520, 0.0001)
  }

  @Test
  fun testUsGAlFtsBug() {
    assertThat(ImperialConversion.litre2usGallon(9.7)).isCloseTo(2.56246, 0.0001)
  }

  @Test
  fun testCubicFeet() {
    assertThat(ImperialConversion.litre2cubicFoot(1.0)).isCloseTo(0.035314724, 0.0001)
    assertThat(ImperialConversion.cubicFoot2litre(1.0)).isCloseTo(28.316846592, 0.0001)
  }

  @Test
  fun testMeterFoot() {
    assertThat(ImperialConversion.meter2foot(1.0)).isCloseTo(3.28084, 0.001)
    assertThat(ImperialConversion.foot2meter(3.28084)).isCloseTo(1.0, 0.001)

    assertThat(ImperialConversion.meter2foot(17.0)).isCloseTo(55.7743, 0.001)
    assertThat(ImperialConversion.foot2meter(55.7743)).isCloseTo(17.0, 0.001)
  }

  @Test
  fun testInches() {
    assertThat(ImperialConversion.inch2mm(17.0)).isCloseTo(431.8, 0.000001)

    assertThat(ImperialConversion.mm2inch(1.0)).isCloseTo(0.0393701, 0.000001)
    assertThat(ImperialConversion.mm2inch(11.0)).isCloseTo(0.433071, 0.000001)
  }
}
