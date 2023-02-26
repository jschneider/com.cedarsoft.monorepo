import kotlin.test.Test

class BootstrapTests {
  @Test
  fun testLoadCss() {
    kotlinext.js.require("bootstrap/dist/css/bootstrap.css")
  }
}
