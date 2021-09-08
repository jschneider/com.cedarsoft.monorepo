package it.neckar.unit.conversion

import assertk.*
import assertk.assertions.*
import com.cedarsoft.unit.si.degC
import com.cedarsoft.unit.si.degF
import org.junit.jupiter.api.Test

class TemperatureConversionTest {
  @Test
  fun testConversion() {
    testRound(5600.0, 10112.0)
    testRound(100.0, 212.0)
    testRound(30.0, 86.0)
    testRound(0.0, 32.0)
  }

  private fun testRound(celsius: @degC Double, fahrenheit: @degF Double) {
    assertThat(TemperatureConversion.celsius2fahrenheit(celsius), "converting $celsius °C").isEqualTo(fahrenheit)
    assertThat(TemperatureConversion.fahrenheit2celsius(fahrenheit), "converting $fahrenheit °F").isEqualTo(celsius)
  }
}
