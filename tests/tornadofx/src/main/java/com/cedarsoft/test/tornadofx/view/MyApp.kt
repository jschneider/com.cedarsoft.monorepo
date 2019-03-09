package com.cedarsoft.test.tornadofx.view

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

import com.cedarsoft.commons.tornadofx.migPane
import javafx.stage.Stage
import tornadofx.*

fun main() {
  launch<TorandoFxTestApp>()
}


class TorandoFxTestApp : App(HelloWorldView::class) {
  override fun init() {
    super.init()
  }

  override fun start(stage: Stage) {
    super.start(stage)
    stage.width = 1024.0
    stage.height = 800.0

    println("start called")
  }
}

class HelloWorldView : View() {
  override val root = migPane {
    label("Hello World")
  }
}
