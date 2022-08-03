package com.cedarsoft.common.kotlin.lang

import com.cedarsoft.test.utils.RandomWithSeed
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt

/**
 * This demo prints the generated random normal values
 */
class NormalizedDemo {
  @Test
  @RandomWithSeed(123)
  fun testNormalPlausibility() {
    val randomValuesCount = IntArray(2000) {
      randomNormal(100.0, 10.0).roundToInt()
    }.sorted()
      .groupingBy {
        it
      }.eachCount()

    for (i in 50..150) {
      val count = randomValuesCount[i] ?: 0

      print("$i".padStart(4))
      print("$count".padStart(4))
      print(": ")
      println("#".repeat(count))
    }
  }
}
