package com.cedarsoft

import kotlin.js.Console

external interface ExtendedConsole : Console {
  fun time(label: String)
  fun timeEnd(label: String)
}

class Profiler {

  private val extendedConsole = console.unsafeCast<ExtendedConsole>()

  private fun measure(label: String, fn: () -> Unit) {
    extendedConsole.time(label)
    fn.invoke()
    extendedConsole.timeEnd(label)
  }

  private fun addLongs(count: Int): Long {
    var sum = 0L
    for (i in 0 until count) {
      sum += i
    }
    return sum
  }

  private fun addInts(count: Int): Int {
    var sum = 0
    for (i in 0 until count) {
      sum += i
    }
    return sum
  }

  private fun addDoubles(count: Int): Double {
    var sum = 0.0
    for (i in 0 until count) {
      sum += i
    }
    return sum
  }

  fun profileAdding(runs: Int) {
    measure("addLongs") {
      for (i in 1..runs) {
        addLongs(1_000_000)
      }
    }

    measure("addInts") {
      for (i in 1..runs) {
        addInts(1_000_000)
      }
    }

    measure("addDoubles") {
      for (i in 1..runs) {
        addDoubles(1_000_000)
      }
    }
  }

  private fun divideAndMultiplyLongs(count: Int): Long {
    var division = 12345678L
    for (i in 1..count) {
      division /= i
      division *= i
    }
    return division
  }

  private fun divideAndMultiplyInts(count: Int): Int {
    var division = 12345678
    for (i in 1..count) {
      division /= i
      division *= i
    }
    return division
  }

  private fun divideAndMultiplyDoubles(count: Int): Double {
    var division = 12345678.0
    for (i in 1..count) {
      division /= i
      division *= i
    }
    return division
  }

  fun profileDivideAndMultiply(runs: Int) {
    measure("divideAndMultiplyLongs") {
      for (i in 1..runs) {
        divideAndMultiplyLongs(1_000_000)
      }
    }

    measure("divideAndMultiplyInts") {
      for (i in 1..runs) {
        divideAndMultiplyInts(1_000_000)
      }
    }

    measure("divideAndMultiplyDoubles") {
      for (i in 1..runs) {
        divideAndMultiplyDoubles(1_000_000)
      }
    }
  }
}

@Suppress("unused") // access via Javascript
fun startProfiling() {
  Profiler().profileAdding(5)
  Profiler().profileDivideAndMultiply(5)
}
