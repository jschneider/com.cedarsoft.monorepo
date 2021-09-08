package it.neckar.unit.conversion

import com.cedarsoft.unit.si.degC
import com.cedarsoft.unit.si.degF


/**
 * Converts temperatures
 */
object TemperatureConversion {
  fun celsius2fahrenheit(celsius: @degC Double): @degF Double {
    return celsius * 1.8 + 32
  }

  fun fahrenheit2celsius(fahrenheit: @degF Double): @degC Double {
    return (fahrenheit - 32) / 1.8
  }

}
