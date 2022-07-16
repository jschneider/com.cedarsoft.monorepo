package com.cedarsoft.commons.provider.boxing

import com.cedarsoft.commons.provider.Index
import com.cedarsoft.commons.provider.IndexProvider
import org.junit.jupiter.api.Test

/**
 * This test can be used to check boxing for providers.
 *
 * * execute the test (within IDEA or by running gradle jsTest)
 * * open `/tmp/_karma_webpack_{*}/common.js`
 * * search for the string from the valueAt method
 * * try to figure out if the return value is boxed
 */
class IndexProviderBoxingTest {
  @Test
  fun testBoxing() {
    println("Testing a SizedProvider with Int return type")
    val indexProvider1: IndexProvider<MyValueClassIndex> = object : IndexProvider<MyValueClassIndex> {
      override fun size(): Int {
        return 7
      }

      override fun valueAt(index: Int): MyValueClassIndex {
        println("this is the valueAt method. Returns an MyValueClassIndex")
        return MyValueClassIndex(index * 3) //Is *not* boxed! (1.7.10)
      }
    }

    val indexProvider2: IndexProvider<MyDataClassIndex> = object : IndexProvider<MyDataClassIndex> {
      override fun size(): Int {
        return 7
      }

      override fun valueAt(index: Int): MyDataClassIndex {
        println("this is the valueAt method. Returns an MyDataClassIndex")
        return MyDataClassIndex(index * 3) //New object is instantiated (of course, since it is a data class)
      }
    }
  }
}

@JvmInline
value class MyValueClassIndex(override val value: Int) : Index {
}

data class MyDataClassIndex(override val value: Int) : Index {
}
