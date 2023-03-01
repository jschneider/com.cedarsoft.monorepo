package it.neckar.open.collections

import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 */
class Array2Test {
  @Test
  fun testSimpleOrientation() {
    val array2 = IntArray2.withGen(3, 4) { x, y ->
      x * 100 + y
    }

    assertThat(array2.width).isEqualTo(3)
    assertThat(array2.height).isEqualTo(4)

    assertThat(array2.data[0]).isEqualTo(0) //x == 0, y == 0
    assertThat(array2.data[1]).isEqualTo(100) //x == 1, y == 0
    assertThat(array2.data[2]).isEqualTo(200) //x == 2, y == 0

    assertThat(array2.data[3]).isEqualTo(1) //x == 0, y == 1
    assertThat(array2.data[4]).isEqualTo(101) //x == 1, y == 1
    assertThat(array2.data[5]).isEqualTo(201) //x == 2, y == 1

    assertThat(array2.data[5]).isEqualTo(array2[2, 1])
  }

  @Test
  fun testIt() {
    val array2 = IntArray2.withGen(100, 50) { x, y ->
      x * 100 + y
    }

    assertThat(array2.width).isEqualTo(100)
    assertThat(array2.height).isEqualTo(50)

    assertThat(array2[3, 4]).isEqualTo(3 * 100 + 4)
  }

  @Test
  fun testResize() {
    val array1 = IntArray2.withGen(100, 50) { x, y ->
      x * 100 + y
    }

    assertThat(array1.width).isEqualTo(100)
    assertThat(array1.height).isEqualTo(50)

    assertThat(array1[99, 49]).isEqualTo(99 * 100 + 49)

    val array2 = array1.withHeight(51)
    assertThat(array2.width).isEqualTo(100)
    assertThat(array2.height).isEqualTo(51)

    //Check the old array has not been changed
    assertThat(array1[0, 0]).isEqualTo(0)
    assertThat(array1[1, 0]).isEqualTo(100)
    assertThat(array1[0, 1]).isEqualTo(1)
    assertThat(array1[1, 1]).isEqualTo(101)
    assertThat(array1[99, 49]).isEqualTo(99 * 100 + 49)

    //Array 2
    assertThat(array2[0, 0]).isEqualTo(0)
    assertThat(array2[1, 0]).isEqualTo(100)
    assertThat(array2[0, 1]).isEqualTo(1)
    assertThat(array2[99, 49]).isEqualTo(99 * 100 + 49)

    //New value - uninitialized
    assertThat(array2[99, 50]).isEqualTo(0)
  }
}
