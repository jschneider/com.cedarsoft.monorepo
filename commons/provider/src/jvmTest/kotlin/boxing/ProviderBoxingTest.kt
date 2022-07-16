package com.cedarsoft.commons.provider.boxing

import com.cedarsoft.commons.provider.SizedProvider
import org.junit.jupiter.api.Test

/**
 * Can be used to check for boxing in the JVM code
 *
 * How to check it:
 * - In IntellIJ IDEA: Tools - Kotlin - Show Kotlin Bytecode
 * - In the tool window press "Decompile"
 * - try to figure out if the value has been boxed
 */
class ProviderBoxingTest {
  @Test
  fun testBoxing() {
    println("Testing a SizedProvider with Int return type")
    val sizedProviderInt: SizedProvider<Int> = object : SizedProvider<Int> {
      override fun size(): Int {
        return 7
      }

      //This is boxed in the JVM
      override fun valueAt(index: Int): Int {
        println("this is the valueAt method. Returns an Int")
        return index * 3 //Is boxed! (1.7.10)
      }
    }
  }
}
