package com.cedarsoft.test.utils

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

class AssertkExtensionsKtTest {
  @Test
  fun testNaN() {
    assertThat {
      assertThat(70.0).isNaN()
    }.isFailure().hasMessage("expected to be NaN but was <70.0>")
  }

  @Test
  fun testAssertFirst() {
    listOf("a", "b").let {
      assertThat(it).all {
        first {
          it.isEqualTo("a")
        }
        last {
          it.isEqualTo("b")
        }
      }

      assertThat {
        assertThat(it).first {
          it.isEqualTo("INVALID")
        }
      }.all {
        isFailure().hasMessage("""expected [["a"]]:<"[INVALID]"> but was:<"[a]"> (["a", "b"])""")
      }

      assertThat {
        assertThat(it).last {
          it.isEqualTo("INVALID")
        }
      }.isFailure().hasMessage("""expected [["b"]]:<"[INVALID]"> but was:<"[b]"> (["a", "b"])""")
    }
  }
}
