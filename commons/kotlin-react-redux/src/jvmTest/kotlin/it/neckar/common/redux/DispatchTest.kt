package it.neckar.common.redux

import assertk.*
import assertk.assertions.*
import it.neckar.common.redux.Dispatch.registerDispatchAction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DispatchTest {
  @BeforeEach
  fun setUp() {
    Dispatch.disposeDispatchAction()
  }

  @AfterEach
  fun tearDown() {
    Dispatch.disposeDispatchAction()
  }

  @Test
  fun testUninitialized() {
    assertThat {
      dispatch(object : StateAction<Int> {
        override fun Int.updateState(): Int {
          throw UnsupportedOperationException("Must not be called")
        }
      })
    }.isFailure().messageContains("No dispatch action configured")
  }

  @Test
  fun testDispatchAction() {
    var state = ""

    registerDispatchAction { stateAction ->
      with(stateAction) {
        state = state.updateState()
      }
    }

    val action = object : StateAction<String> {
      override fun String.updateState(): String {
        return this + "a"
      }
    }

    assertThat(state).isEqualTo("")
    dispatch(action)
    assertThat(state).isEqualTo("a")
    dispatch(action)
    dispatch(action)
    assertThat(state).isEqualTo("aaa")
  }
}
