package com.cedarsoft.exceptions.handling

import assertk.*
import assertk.assertions.*
import com.cedarsoft.commons.javafx.FxUtils
import com.cedarsoft.test.utils.DisableIfHeadless
import javafx.stage.Stage
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testfx.framework.junit5.ApplicationTest
import org.testfx.util.WaitForAsyncUtils

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@DisableIfHeadless
class ApplicationExceptionFxDialogTest : ApplicationTest() {

  override fun start(stage: Stage?) {
    super.start(stage)

    val exception = MyTestException(MyTestException.TestExceptionDetails.ERROR_1, "asdf")

    ApplicationExceptionFxDialog(exception).show()
  }

  @Disabled
  @Test
  internal fun testAppFxExceptionDialog() {
    println("Thread: ${Thread.currentThread().name}")

    val targetWindow = targetWindow(0)
    val smallWidth = targetWindow.targetWindow().width

    assertThat(smallWidth).isLessThan(400.0)

    targetWindow.clickOn(".details-button")

    if (false) {
      FxUtils.dump(targetWindow().scene.root, System.out)
    }

    WaitForAsyncUtils.waitForFxEvents()
    assertThat(targetWindow.targetWindow().width).isGreaterThan(400.0)
  }
}
