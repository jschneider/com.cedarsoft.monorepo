package it.neckar.open.observable

import assertk.*
import assertk.assertions.*
import org.junit.jupiter.api.Test
import kotlin.system.measureNanoTime
import kotlin.time.nanoseconds

/**
 *
 */
class ObservableObjectPerformanceDemo {

  val count = 10_000_000

  @Test
  fun testDirectAccess() {
    val performanceClass = MyPerformanceClass()

    var sum = 0.0

    val direct = measureNanoTime {
      for (i in 0 until count) {
        sum += performanceClass.varValue
      }
    }

    println("Took ${direct.nanoseconds.toLongMilliseconds()} ms")

    assertThat(sum).isEqualTo(count.toDouble())
  }

  @Test
  fun testObservableObject() {
    val performanceClass = MyPerformanceClass()

    var sum = 0.0

    val direct = measureNanoTime {
      for (i in 0 until count) {
        sum += performanceClass.observableValue.value
      }
    }

    println("Took ${direct.nanoseconds.toLongMilliseconds()} ms")
    assertThat(sum).isEqualTo(count.toDouble())
  }
}

private class MyPerformanceClass {
  var varValue: Double = 1.0
  var observableValue = ObservableDouble(1.0)

}
