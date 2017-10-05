package com.cedarsoft.test.tornadofx.view

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.stage.Stage
import org.assertj.core.api.Assertions

/**
 * @Author : Val
 * @Created : 2016-01-12
 * @E-mail : valer@126.com
 * @Version : 1.0
 */
fun main(args: Array<String>) {
  Application.launch(Main::class.java, *args)
}

class Main : Application() {

  override fun start(primaryStage: Stage) {
    val resource = Main::class.java.getResource("/bootstrap3overview.fxml")
    Assertions.assertThat(resource).isNotNull()
    val loader = FXMLLoader(resource)
    loader.load<Any>()

    primaryStage.scene = Scene(loader.getRoot<ScrollPane>(), 800.0, 600.0)

    primaryStage.show()
  }
}