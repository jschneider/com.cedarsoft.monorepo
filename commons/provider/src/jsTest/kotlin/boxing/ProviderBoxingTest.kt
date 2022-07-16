package com.cedarsoft.commons.provider.boxing

import com.cedarsoft.commons.provider.SizedProvider
import kotlin.test.Test

/**
 * This test can be used to check boxing for providers.
 *
 * * execute the test (within IDEA or by running gradle jsTest)
 * * open `/tmp/_karma_webpack_{*}/common.js`
 * * search for the string from the valueAt method
 * * try to figure out if the return value is boxed
 */
class ProviderBoxingTest {
  @Test
  fun testBoxing() {
    println("Testing a SizedProvider with Int return type")
    val sizedProviderInt: SizedProvider<Int> = object : SizedProvider<Int> {
      override fun size(): Int {
        return 7
      }

      override fun valueAt(index: Int): Int {
        println("this is the valueAt method. Returns an Int")
        return index * 3
      }
    }
  }
}
