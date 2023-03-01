package it.neckar.open.observable

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import kotlin.test.fail

internal class ObservableBooleanTest {
  @Test
  internal fun testConstructor() {
    assertThat(ObservableBoolean(false).value).isFalse()
    assertThat(ObservableBoolean(true).value).isTrue()
  }

  @Test
  internal fun testIt() {
    val observableBoolean = ObservableBoolean(false)
    assertThat(observableBoolean.value).isFalse()


    var called = false
    observableBoolean.consumeChanges { oldValue, newValue ->
      assertThat(called).isFalse()
      called = true
      assertThat(oldValue).isFalse()
      assertThat(newValue).isTrue()
    }

    assertThat(called).isFalse()

    //Nothing should happen since the value has not changed
    observableBoolean.value = false
    assertThat(called).isFalse()

    observableBoolean.value = true
    assertThat(called).isTrue()
  }

  @Test
  internal fun testOr() {
    val o1 = ObservableBoolean(false)
    val o2 = ObservableBoolean(false)

    val or = o1 or o2
    val and = o1 and o2

    assertThat(or.value).isFalse()
    assertThat(and.value).isFalse()
    o1.value = true
    assertThat(or.value).isTrue()
    assertThat(and.value).isFalse()
    o2.value = true
    assertThat(or.value).isTrue()
    assertThat(and.value).isTrue()

    o1.value = false
    assertThat(or.value).isTrue()
    assertThat(and.value).isFalse()
    o2.value = false
    assertThat(or.value).isFalse()
    assertThat(and.value).isFalse()
  }

  @Test
  internal fun testMultipleOrAnd() {
    val o1 = ObservableBoolean(false)
    val o2 = ObservableBoolean(false)
    val o3 = ObservableBoolean(false)

    val or = o1.or(o2, o3)
    val and = o1.and(o2, o3)

    assertThat(or.value).isFalse()
    assertThat(and.value).isFalse()

    o1.value = true
    o2.value = true
    o3.value = true
    assertThat(or.value).isTrue()
    assertThat(and.value).isTrue()

    o1.value = false
    o2.value = false
    o3.value = true
    assertThat(or.value).isTrue()
    assertThat(and.value).isFalse()

    o1.value = true
    o2.value = false
    o3.value = false
    assertThat(or.value).isTrue()
    assertThat(and.value).isFalse()

    o1.value = false
    o2.value = false
    o3.value = false
    assertThat(or.value).isFalse()
    assertThat(and.value).isFalse()
  }

  @Test
  internal fun testListOr() {
    val o1 = ObservableBoolean(false)
    val o2 = ObservableBoolean(false)
    val o3 = ObservableBoolean(false)

    val list = listOf(o1, o2, o3)

    val or = list.or()
    assertThat(or.value).isFalse()

    o1.value = true
    assertThat(or.value).isTrue()

    o2.value = true
    assertThat(or.value).isTrue()

    o1.value = false
    assertThat(or.value).isTrue()
  }

  @Test
  internal fun testListAnd() {
    val o1 = ObservableBoolean(false)
    val o2 = ObservableBoolean(false)
    val o3 = ObservableBoolean(false)

    val list = listOf(o1, o2, o3)

    val and = list.and()
    assertThat(and.value).isFalse()

    o1.value = true
    assertThat(and.value).isFalse()
    o2.value = true
    assertThat(and.value).isFalse()
    o3.value = true
    assertThat(and.value).isTrue()
  }

  @Test
  internal fun testEmptyListOr() {
    val list = listOf<ObservableBoolean>()
    try {
      list.or()
      fail("where is the exception")
    } catch (e: Exception) {
    }
    try {
      list.and().value
      fail("where is the exception")
    } catch (e: Exception) {
    }
  }
}
