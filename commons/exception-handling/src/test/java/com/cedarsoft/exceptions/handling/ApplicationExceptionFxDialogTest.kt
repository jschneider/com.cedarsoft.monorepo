package com.cedarsoft.exceptions.handling

import com.cedarsoft.commons.javafx.FxUtils
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class ApplicationExceptionFxDialogTest : ApplicationTest() {

  override fun start(stage: Stage?) {
    super.start(stage)

    val exception = MyTestException(
      MyTestException.TestExceptionDetails.ERROR_1, "asdf")

    ApplicationExceptionFxDialog(exception).show()

  }

  @Test
  internal fun testAppFxExceptionDialog() {
    val targetWindow = targetWindow(0)

    println("Children: ${targetWindow().scene.root.childrenUnmodifiable}")


    //    targetWindow.clickOn(".button")
    targetWindow.clickOn(".details-button")

    FxUtils.dump(targetWindow().scene.root, System.out)

    Thread.sleep(10000)
  }
}
