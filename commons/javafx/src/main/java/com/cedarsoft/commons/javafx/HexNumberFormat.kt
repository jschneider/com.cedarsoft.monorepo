package com.cedarsoft.commons.javafx

import java.text.FieldPosition
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale

/**
 * formats integers to/from hex string
 */
class HexNumberFormat : NumberFormat() {
  override fun format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
    throw IllegalArgumentException("doubles not supported.")
  }

  override fun format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
    return toAppendTo.append("0x").append(String.format(Locale.getDefault(), "%08X", number))
  }

  override fun parse(source: String, parsePosition: ParsePosition): Number {
    if (source.startsWith("0x")) {
      try {
        return source.substring(2).toLong(16)
      } catch (e: NumberFormatException) {
        e.printStackTrace()
      }
      return 0
    }

    try {
      return source.toLong(16)
    } catch (e: NumberFormatException) {
      e.printStackTrace()
    }
    return 0
  }
}
