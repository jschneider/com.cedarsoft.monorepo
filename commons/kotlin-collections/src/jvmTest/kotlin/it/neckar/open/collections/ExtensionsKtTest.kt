package it.neckar.open.collections

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class ExtensionsKtTest {
  @Test
  fun testInvoke() {

    IntArray2.invokeCols(
      arrayOf(
        intArrayOf(1, 2, 3),
        intArrayOf(10, 20, 30),
        intArrayOf(100, 200, 300),
        intArrayOf(1000, 2000, 3000)
      )
    ).let {
      assertThat(it.width).isEqualTo(4)
      assertThat(it.height).isEqualTo(3)

      assertThat(it[0, 0]).isEqualTo(1)
      assertThat(it[0, 1]).isEqualTo(2)
      assertThat(it[0, 2]).isEqualTo(3)

      assertThat(it[1, 0]).isEqualTo(10)
      assertThat(it[1, 1]).isEqualTo(20)
      assertThat(it[1, 2]).isEqualTo(30)
    }
  }
}
