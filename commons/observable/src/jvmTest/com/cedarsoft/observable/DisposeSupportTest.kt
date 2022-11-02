package com.cedarsoft.observable

import assertk.*
import assertk.assertions.*
import com.cedarsoft.dispose.Disposable
import com.cedarsoft.dispose.DisposeSupport
import org.junit.jupiter.api.Test

class DisposeSupportTest {
  @Test
  fun testDisposedState() {
    val disposeSupport = DisposeSupport()

    assertThat(disposeSupport.disposed).isFalse()
    disposeSupport.dispose()

    try {
      disposeSupport.onDispose { }
      fail("Must not be called")
    } catch (e: Exception) {
    }
  }

  @Test
  fun testCall() {
    val disposeSupport = DisposeSupport()


    var called = false
    disposeSupport.onDispose {
      assertThat(called).isFalse()
      called = true
    }

    assertThat(called).isFalse()
    disposeSupport.dispose()
    assertThat(called).isTrue()
  }

  @Test
  fun testAll() {
    var called0: Boolean = false
    var called1: Boolean = false

    Disposable.all(
      Disposable {
        called0 = true
      },
      Disposable {
        called1 = true
      },
    ).dispose()

    assertThat(called0).isTrue()
    assertThat(called1).isTrue()
  }
}
