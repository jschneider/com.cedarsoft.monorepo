package it.neckar.open.collections

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class _ExtensionsKtTest {
  @Test
  fun testRight() {
    //Never returns 1 (is exclusive)
    assertThat(genericBinarySearchRight(0, 1) { -1 }).isEqualTo(0)
    assertThat(genericBinarySearchRight(0, 1) { 0 }).isEqualTo(0)
    assertThat(genericBinarySearchRight(0, 1) { 1 }).isEqualTo(0)

    //Return at most 1
    assertThat(genericBinarySearchRight(0, 2) { -1 }).isEqualTo(1)
    assertThat(genericBinarySearchRight(0, 2) { 0 }).isEqualTo(0)
    assertThat(genericBinarySearchRight(0, 2) { 1 }).isEqualTo(0)

    //Return at most 7
    assertThat(genericBinarySearchRight(0, 8) { -1 }).isEqualTo(7)
    assertThat(genericBinarySearchRight(0, 8) { 0 }).isEqualTo(3) //the first index that is questioned
    assertThat(genericBinarySearchRight(0, 8) { 1 }).isEqualTo(0)

    assertThat(genericBinarySearchRight(0, 10) { -1 }).isEqualTo(9)
  }

  @Test
  fun testLeft2() {
    //Never returns 1 (is exclusive)
    assertThat(genericBinarySearchLeft(0, 1) { -1 }).isEqualTo(0)
    assertThat(genericBinarySearchLeft(0, 1) { 0 }).isEqualTo(0)
    assertThat(genericBinarySearchLeft(0, 1) { 1 }).isEqualTo(0)

    //Return at most 1
    assertThat(genericBinarySearchLeft(0, 2) { -1 }).isEqualTo(1)
    assertThat(genericBinarySearchLeft(0, 2) { 0 }).isEqualTo(0)
    assertThat(genericBinarySearchLeft(0, 2) { 1 }).isEqualTo(0)

    //Return at most 7
    assertThat(genericBinarySearchLeft(0, 8) { -1 }).isEqualTo(7)
    assertThat(genericBinarySearchLeft(0, 8) { 0 }).isEqualTo(3) //the first index that is questioned
    assertThat(genericBinarySearchLeft(0, 8) { 1 }).isEqualTo(0)
  }

  @Test
  fun testLeftRight() {
    //Exact hit
    assertThat(genericBinarySearchLeft(0, 3) { it.compareTo(1.0) }).isEqualTo(1)
    assertThat(genericBinarySearchRight(0, 3) { it.compareTo(1.0) }).isEqualTo(1)

    //between two values - returning different values for left/right
    assertThat(genericBinarySearchLeft(0, 3) { it.compareTo(1.5) }).isEqualTo(1)
    assertThat(genericBinarySearchRight(0, 3) { it.compareTo(1.5) }).isEqualTo(2)

    //very small - left most index
    assertThat(genericBinarySearchLeft(0, 3) { 1 }).isEqualTo(0)
    assertThat(genericBinarySearchRight(0, 3) { 1 }).isEqualTo(0)

    //very large - right most index
    assertThat(genericBinarySearchLeft(0, 3) { -1 }).isEqualTo(2)
    assertThat(genericBinarySearchRight(0, 3) { -1 }).isEqualTo(2)
  }

  @Test
  fun testLeft() {
    assertThat(genericBinarySearchRight(0, 10) { it.compareTo(10.5) }).isEqualTo(9)
  }

  @Test
  fun testBounds() {
    genericBinarySearchRight(1, 20) {
      it.compareTo(0)
    }.let {
      assertThat(it).isEqualTo(1)
    }

    genericBinarySearchLeft(1, 20) {
      it.compareTo(0)
    }.let {
      assertThat(it).isEqualTo(1)
    }

    genericBinarySearchLeft(1, 2) {
      it.compareTo(0)
    }.let {
      assertThat(it).isEqualTo(1)
    }
    genericBinarySearchRight(1, 2) {
      it.compareTo(0)
    }.let {
      assertThat(it).isEqualTo(1)
    }
  }

  @Test
  fun testBiSect() {
    genericBinarySearchRight(0, 20) {
      it.compareTo(4)
    }.let {
      assertThat(it).isEqualTo(4)
    }

    genericBinarySearchRight(0, 20) {
      it.compareTo(17)
    }.let {
      assertThat(it).isEqualTo(17)
    }

    genericBinarySearchRight(0, 20) {
      it.compareTo(4.5)
    }.let {
      assertThat(it).isEqualTo(5)
    }

    genericBinarySearchLeft(0, 20) {
      it.compareTo(4.5)
    }.let {
      assertThat(it).isEqualTo(4)
    }
  }
}
