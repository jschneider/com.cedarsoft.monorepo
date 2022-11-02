package com.cedarsoft.observable

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test

/**
 */
internal class ObservableObjectTest {
  @Test
  fun testMap2() {
    val observableObject0 = ObservableObject(7)
    val observableObject1 = ObservableObject("asdf")
    val observableObject2 = ObservableObject(27.0)

    val mapped = observableObject0.map(
      observableObject1,
      observableObject2,
    ) { a, b, c ->
      "$a - $b - $c"
    }

    assertThat(mapped.value).isEqualTo("7 - asdf - 27.0")
    observableObject0.value = 8
    assertThat(mapped.value).isEqualTo("8 - asdf - 27.0")
    observableObject1.value = "foo"
    assertThat(mapped.value).isEqualTo("8 - foo - 27.0")
    observableObject2.value = 11.0
    assertThat(mapped.value).isEqualTo("8 - foo - 11.0")
  }

  @Test
  internal fun testMapWithEvent() {
    val observableObject = ObservableObject("Hey")
    val lengthProperty = observableObject.map {
      it.length
    }

    assertThat(lengthProperty.value).isEqualTo(3)

    var result = -1
    var called = 0

    lengthProperty.consume(false) {
      ++called
      result = it
    }

    //Not yet called!
    assertThat(called).isEqualTo(0)
    assertThat(result).isEqualTo(-1)

    observableObject.value = "foobar"
    assertThat(lengthProperty.value).isEqualTo(6)

    assertThat(called).isEqualTo(1)
    assertThat(result).isEqualTo(6)
  }

  @Test
  fun testSameValueEvent() {
    val observableObject = ObservableObject("Hey")

    val updates = mutableListOf<String>()
    observableObject.consume {
      updates.add(it)
    }

    assertThat(updates).isEmpty()

    observableObject.value = "Hey"
    assertThat(updates).isEmpty() //no update expected, since it is the same value

    observableObject.value = "Other"
    assertThat(updates).containsExactly("Other") //Update
  }

  @Test
  fun testGenericsReadOnly() {
    var number: ReadOnlyObservableObject<Number> = ObservableObject<Number>(17.0)
    val double: ReadOnlyObservableObject<Double> = ObservableObject(17.0)
    val int: ReadOnlyObservableObject<Int> = ObservableObject(17)

    //Assignments to number should be possible
    number = double
    number = int
  }

  @Test
  internal fun testDelegates() {
    val observableObject = ObservableObject("asdf")
    assertThat(observableObject.value).isEqualTo("asdf")

    observableObject.value = "2222"

    assertThat(observableObject.value).isEqualTo("2222")
  }

  @Test
  internal fun testDelegate() {
    val myObject = object : Any() {
      val nameProperty = ObservableObject("asdf")
      var name by nameProperty
    }

    assertThat(myObject.name).isEqualTo("asdf")
    myObject.name = "777"
    assertThat(myObject.name).isEqualTo("777")
  }

  @Test
  internal fun testBidirectionalBinding() {
    val first = ObservableObject("1")
    val second = ObservableObject("2")

    first.bindBidirectional(second)

    assertThat(first.value).isEqualTo("2")
    assertThat(second.value).isEqualTo("2")

    first.value = "77"

    assertThat(first.value).isEqualTo("77")
    assertThat(second.value).isEqualTo("77")

    second.value = "99"

    assertThat(first.value).isEqualTo("99")
    assertThat(second.value).isEqualTo("99")
  }

  @Test
  internal fun testUnidirectionalBinding() {
    val first = ObservableObject("1")
    val second = ObservableObject("2")

    first.bind(second)

    assertThat(first.value).isEqualTo("2")
    assertThat(second.value).isEqualTo("2")

    first.value = "a"
    assertThat(first.value).isEqualTo("a")
    assertThat(second.value).isEqualTo("2")

    second.value = "b"
    assertThat(first.value).isEqualTo("b")
    assertThat(second.value).isEqualTo("b")
  }

  @Test
  internal fun testBindTwo() {
    val enabled = ObservableBoolean(false)
    val count = ObservableInt(0)


    val result: ReadOnlyObservableObject<String> = enabled.map(count) { enabledValue, countValue ->
      "$enabledValue - $countValue"
    }

    assertThat(result.value).isEqualTo("false - 0")
    enabled.value = true
    assertThat(result.value).isEqualTo("true - 0")
    count.value = 17
    assertThat(result.value).isEqualTo("true - 17")

    //must not be published, when no value has been changed
    result.consumeChanges { _, _ ->
      fail("must not be called")
    }

    count.value = 17
    enabled.value = true
  }

  @Test
  internal fun testMap() {
    val enabled = ObservableBoolean(false)

    val mapped = enabled.map {
      it.toString()
    }

    assertThat(mapped.value).isEqualTo("false")

    mapped.consumeChanges { _, _ ->
      fail("must not be called")
    }

    //Repeat the same value - must not be published
    enabled.value = false
    enabled.value = false
    enabled.value = false
  }

  @Test
  internal fun testMerge() {
    val o1 = ObservableBoolean(false)
    val o2 = ObservableBoolean(false)
    val o3 = ObservableBoolean(false)

    val merged = reduceObservables(o1, o2, o3) {
      it.reduce { b1, b2 ->
        b1 || b2
      }
    }

    assertThat(merged.value).isFalse()

    o1.value = false
    o2.value = true
    o3.value = false
    assertThat(merged.value).isTrue()

    o1.value = false
    o2.value = false
    o3.value = false
    assertThat(merged.value).isFalse()

    o1.value = false
    o2.value = false
    o3.value = true
    assertThat(merged.value).isTrue()
  }

  @Test
  internal fun testReduce2() {
    val o1 = ObservableBoolean(false)
    val o2 = ObservableBoolean(false)
    val o3 = ObservableBoolean(false)

    val merged = o1.reduce(o2, o3) {
      it.reduce { b1, b2 ->
        b1 || b2
      }
    }

    assertThat(merged.value).isFalse()

    o1.value = false
    o2.value = true
    o3.value = false
    assertThat(merged.value).isTrue()

    o1.value = false
    o2.value = false
    o3.value = false
    assertThat(merged.value).isFalse()

    o1.value = false
    o2.value = false
    o3.value = true
    assertThat(merged.value).isTrue()
  }

  @Test
  fun testConsumeImmediately() {
    val observableInt = ObservableInt(4711)
    var result = 1
    var called = 0
    observableInt.consume(true) {
      ++called
      result = it
    }
    assertThat(result).isEqualTo(4711)
    assertThat(called).isEqualTo(1)

    observableInt.value = 666
    assertThat(result).isEqualTo(666)
    assertThat(called).isEqualTo(2)
  }

  @Test
  fun testConsumeImmediately2() {
    val observableInt = ObservableInt(4711)
    var result = 1
    var called = 0
    observableInt.consume(false) {
      ++called
      result = it
    }

    assertThat(result).isEqualTo(1)
    assertThat(called).isEqualTo(0)

    observableInt.value = 4711
    assertThat(result).isEqualTo(1)
    assertThat(called).isEqualTo(0)

    observableInt.value = 666
    assertThat(result).isEqualTo(666)
    assertThat(called).isEqualTo(1)
  }
}
