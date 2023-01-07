import it.neckar.commons.kotlin.js.debug
import kotlin.test.Test

class ConsoleDebugDemo {
  @Test
  fun testDebug() {
    console.debug("Hello Debug Console!")
    console.debug("Hello Debug Console!", "Another object")
    console.debug("Hello Debug Console!", object {
      val a = 1
      val b = "foo"
    })

    console.info("Hello Info Console!")
    console.info("Hello Info Console!", "Another object")
    console.info("Hello Info Console!", object {
      val a = 1
      val b = "foo"
    })

    console.log("Hello log Console!")
    console.log("Hello log Console!", "Another object")
    console.log("Hello log Console!", object {
      val a = 1
      val b = "foo"
    })
  }
}
