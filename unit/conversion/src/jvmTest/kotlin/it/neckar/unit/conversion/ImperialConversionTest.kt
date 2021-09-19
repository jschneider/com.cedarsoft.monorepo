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
  fun testCubicFeet() {
    assertThat(ImperialConversion.litre2cubicFoot(1.0)).isCloseTo(0.035314724, 0.0001)
    assertThat(ImperialConversion.cubicFoot2litre(1.0)).isCloseTo(28.316846592, 0.0001)
  }

  @Test
  fun testMeterFoot() {
    assertThat(ImperialConversion.meter2foot(1.0)).isCloseTo(0.3048, 0.0001)
  }
}
