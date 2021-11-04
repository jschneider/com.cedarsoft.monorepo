package it.neckar.unit.conversion

import com.cedarsoft.unit.other.CubicFoot
import com.cedarsoft.unit.other.UsGallon
import com.cedarsoft.unit.other.`in`
import com.cedarsoft.unit.other.ft
import com.cedarsoft.unit.other.ft.Companion.MM_FEET_RATIO
import com.cedarsoft.unit.si.L
import com.cedarsoft.unit.si.degC
import com.cedarsoft.unit.si.degF
import com.cedarsoft.unit.si.m
import com.cedarsoft.unit.si.mm


/**
 * Converts from/to imperial units
 */
object ImperialConversion {
  fun celsius2fahrenheit(celsius: @degC Double): @degF Double {
    return celsius * 1.8 + 32
  }

  fun fahrenheit2celsius(fahrenheit: @degF Double): @degC Double {
    return (fahrenheit - 32) / 1.8
  }

  fun litre2usGallon(litre: @L Double): @UsGallon Double {
    return litre / UsGallon.US_GALLON_LITRE_RATIO
  }

  fun usGallon2litre(usGallon: @UsGallon Double): @L Double {
    return usGallon * UsGallon.US_GALLON_LITRE_RATIO
  }

  fun litre2cubicFoot(litre: @L Double): @CubicFoot Double {
    return litre / CubicFoot.LITRE_PER_CU_FOOT
  }

  fun cubicFoot2litre(cubicFeet: @CubicFoot Double): @L Double {
    return cubicFeet * CubicFoot.LITRE_PER_CU_FOOT
  }

  /**
   * Converts meter to foot
   */
  fun meter2foot(meter: @m Double): @ft Double {
    return meter / 1000.0 * MM_FEET_RATIO
  }

  /**
   * Converts mm to inch
   */
  fun mm2inch(mm: @mm Double): @`in` Double {
    return mm / `in`.MM_RATIO
  }

  fun inch2mm(inch: @`in` Double): @mm Double {
    return inch * `in`.MM_RATIO
  }
}
