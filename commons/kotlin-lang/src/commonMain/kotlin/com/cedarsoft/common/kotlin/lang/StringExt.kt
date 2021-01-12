package com.cedarsoft.common.kotlin.lang

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

operator fun String.Companion.invoke(arrays: IntArray, offset: Int = 0, size: Int = arrays.size - offset): String {
  val sb = StringBuilder()
  for (n in offset until offset + size) {
    sb.append(arrays[n].toChar()) // @TODO: May not work the same! In JS: String.fromCodePoint
  }
  return sb.toString()
}

fun String_fromIntArray(arrays: IntArray, offset: Int = 0, size: Int = arrays.size - offset): String = String(arrays, offset, size)
fun String_fromCharArray(arrays: CharArray, offset: Int = 0, size: Int = arrays.size - offset): String = String(arrays, offset, size)

////////////////////////////////////
////////////////////////////////////

private val formatRegex = Regex("%([-]?\\d+)?(\\w)")

fun String.splitKeep(regex: Regex): List<String> {
  val str = this
  val out = arrayListOf<String>()
  var lastPos = 0
  for (part in regex.findAll(this)) {
    val prange = part.range
    if (lastPos != prange.start) {
      out += str.substring(lastPos, prange.start)
    }
    out += str.substring(prange)
    lastPos = prange.endInclusive + 1
  }
  if (lastPos != str.length) {
    out += str.substring(lastPos)
  }
  return out
}

private val replaceNonPrintableCharactersRegex by lazy { Regex("[^ -~]") }
fun String.replaceNonPrintableCharacters(replacement: String = "?"): String {
  return this.replace(replaceNonPrintableCharactersRegex, replacement)
}

fun String.indexOfOrNull(char: Char, startIndex: Int = 0): Int? = this.indexOf(char, startIndex).takeIf { it >= 0 }

fun String.lastIndexOfOrNull(char: Char, startIndex: Int = lastIndex): Int? =
  this.lastIndexOf(char, startIndex).takeIf { it >= 0 }

fun String.splitInChunks(size: Int): List<String> {
  val out = arrayListOf<String>()
  var pos = 0
  while (pos < this.length) {
    out += this.substring(pos, kotlin.math.min(this.length, pos + size))
    pos += size
  }
  return out
}

fun String.substr(start: Int): String = this.substr(start, this.length)

fun String.substr(start: Int, length: Int): String {
  val low = (if (start >= 0) start else this.length + start).coerceIn(0, this.length)
  val high = (if (length >= 0) low + length else this.length + length).coerceIn(0, this.length)
  return if (high >= low) this.substring(low, high) else ""
}

inline fun String.eachBuilder(transform: StringBuilder.(Char) -> Unit): String = buildString {
  @Suppress("ReplaceManualRangeWithIndicesCalls") // Performance reasons? Check that plain for doesn't allocate
  for (n in 0 until this@eachBuilder.length) transform(this, this@eachBuilder[n])
}

inline fun String.transform(transform: (Char) -> String): String = buildString {
  @Suppress("ReplaceManualRangeWithIndicesCalls") // Performance reasons? Check that plain for doesn't allocate
  for (n in 0 until this@transform.length) append(transform(this@transform[n]))
}

fun String.parseInt(): Int = when {
  this.startsWith("0x", ignoreCase = true) -> this.substring(2).toLong(16).toInt()
  this.startsWith("0o", ignoreCase = true) -> this.substring(2).toLong(8).toInt()
  this.startsWith("0b", ignoreCase = true) -> this.substring(2).toLong(2).toInt()
  else                                     -> this.toInt()
}

//val String.quoted: String get() = this.quote()
fun String.toCharArray() = CharArray(length) { this@toCharArray[it] }

fun String.escape(): String {
  val out = StringBuilder()
  for (n in 0 until this.length) {
    val c = this[n]
    when (c) {
      '\\'                  -> out.append("\\\\")
      '"'                   -> out.append("\\\"")
      '\n'                  -> out.append("\\n")
      '\r'                  -> out.append("\\r")
      '\t'                  -> out.append("\\t")
      in '\u0000'..'\u001f' -> {
        out.append("\\x")
        out.append(Hex.encodeCharLower(c.toInt().extract(4, 4)))
        out.append(Hex.encodeCharLower(c.toInt().extract(0, 4)))
      }
      else                  -> out.append(c)
    }
  }
  return out.toString()
}

fun String.uescape(): String {
  val out = StringBuilder()
  for (n in 0 until this.length) {
    val c = this[n]
    when (c) {
      '\\' -> out.append("\\\\")
      '"'  -> out.append("\\\"")
      '\n' -> out.append("\\n")
      '\r' -> out.append("\\r")
      '\t' -> out.append("\\t")
      else -> if (c.isPrintable()) {
        out.append(c)
      } else {
        out.append("\\u")
        out.append(Hex.encodeCharLower(c.toInt().extract(12, 4)))
        out.append(Hex.encodeCharLower(c.toInt().extract(8, 4)))
        out.append(Hex.encodeCharLower(c.toInt().extract(4, 4)))
        out.append(Hex.encodeCharLower(c.toInt().extract(0, 4)))
      }
    }
  }
  return out.toString()
}

fun String.unescape(): String {
  val out = StringBuilder()
  var n = 0
  while (n < this.length) {
    val c = this[n++]
    when (c) {
      '\\' -> {
        val c2 = this[n++]
        when (c2) {
          '\\' -> out.append('\\')
          '"'  -> out.append('\"')
          'n'  -> out.append('\n')
          'r'  -> out.append('\r')
          't'  -> out.append('\t')
          'u'  -> {
            val chars = this.substring(n, n + 4)
            n += 4
            out.append(chars.toInt(16).toChar())
          }
          else -> {
            out.append("\\$c2")
          }
        }
      }
      else -> out.append(c)
    }
  }
  return out.toString()
}

fun String?.uquote(): String = if (this != null) "\"${this.uescape()}\"" else "null"
fun String?.quote(): String = if (this != null) "\"${this.escape()}\"" else "null"

fun String.isQuoted(): Boolean = this.startsWith('"') && this.endsWith('"')
fun String.unquote(): String = if (isQuoted()) this.substring(1, this.length - 1).unescape() else this

val String?.quoted: String get() = this.quote()
val String.unquoted: String get() = this.unquote()

