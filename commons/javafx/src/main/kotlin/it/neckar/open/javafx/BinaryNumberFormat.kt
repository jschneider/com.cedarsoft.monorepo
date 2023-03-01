package it.neckar.open.javafx

import java.text.FieldPosition
import java.text.NumberFormat
import java.text.ParsePosition

/**
 * Formats integers and long values to/from binary string
 */
class BinaryNumberFormat(private val minTextLength: Int) : NumberFormat() {
  override fun format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
    throw IllegalArgumentException("doubles not supported.")
  }

  override fun format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer {
    val stringBuffer = toAppendTo.append("0b")
    val binaryString = java.lang.Long.toBinaryString(number)
    val padCount = minTextLength - binaryString.length
    for (i in 0 until padCount) {
      stringBuffer.append('0')
    }
    stringBuffer.append(binaryString)
    return stringBuffer
  }

  override fun parse(source: String, parsePosition: ParsePosition): Number {
    if (source.startsWith("0b")) {
      try {
        return java.lang.Long.parseUnsignedLong(source.substring(2), 2)
      } catch (e: NumberFormatException) {
        e.printStackTrace()
      }
    }
    try {
      return java.lang.Long.parseUnsignedLong(source, 2)
    } catch (e: NumberFormatException) {
      e.printStackTrace()
    }
    return 0
  }
}
