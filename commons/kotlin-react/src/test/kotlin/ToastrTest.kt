import it.neckar.react.common.toast.*
import kotlinext.js.require
import kotlin.test.Test

class ToastrTest {
  @Test
  fun name() {
    //Ensure the toastr object has been loaded
    requireNotNull(toastr)
  }

  @Test
  fun testIt() {
    Toast.info("The info")
  }

  @Test
  fun testResource() {
    require("toastr/build/toastr.css")
    require("toastr/build/toastr.min.css")
  }
}
