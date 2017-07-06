package com.cedarsoft.test.tornadofx

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.application.Application
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.Duration
import tornadofx.App
import tornadofx.Controller
import tornadofx.FX
import tornadofx.Stylesheet
import tornadofx.View
import tornadofx.addClass
import tornadofx.box
import tornadofx.button
import tornadofx.center
import tornadofx.checkbox
import tornadofx.cssclass
import tornadofx.hbox
import tornadofx.importStylesheet
import tornadofx.label
import tornadofx.passwordfield
import tornadofx.px
import tornadofx.row
import tornadofx.singleAssign
import tornadofx.textfield
import tornadofx.top

fun main(args: Array<String>) {
  Application.launch(LoginApp::class.java, *args)
}

class LoginApp : App(LoginScreen::class) {
  val loginController: LoginController by inject()

  override fun start(stage: Stage) {
    importStylesheet(Styles::class)
    super.start(stage)
    loginController.init()
  }
}

class LoginController : Controller() {
  val loginScreen: LoginScreen by inject()
  val workbench: Workbench by inject()

  fun init() {
    with(config) {
      if (containsKey(KEY_USERNAME) && containsKey(KEY_PASSWORD))
        tryLogin(string(KEY_USERNAME), string(KEY_PASSWORD), true)
      else
        showLoginScreen("Please log in")
    }
  }

  fun showLoginScreen(message: String, shake: Boolean = false) {
    if (FX.primaryStage.scene.root != loginScreen.root) {
      FX.primaryStage.scene.root = loginScreen.root
      FX.primaryStage.sizeToScene()
      FX.primaryStage.centerOnScreen()
    }

    loginScreen.title = message

    Platform.runLater {
      loginScreen.username.requestFocus()
      if (shake) loginScreen.shakeStage()
    }
  }

  fun showWorkbench() {
    if (FX.primaryStage.scene.root != workbench.root) {
      FX.primaryStage.scene.root = workbench.root
      FX.primaryStage.sizeToScene()
      FX.primaryStage.centerOnScreen()
    }
  }

  fun tryLogin(username: String, password: String, remember: Boolean) {
    runAsync {
      username == "admin" && password == "secret"
    } ui { successfulLogin ->

      if (successfulLogin) {
        loginScreen.clear()

        if (remember) {
          with(config) {
            set(KEY_USERNAME to username)
            set(KEY_PASSWORD to password)
            save()
          }
        }

        showWorkbench()
      } else {
        showLoginScreen("Login failed. Please try again.", true)
      }
    }
  }

  fun logout() {
    with(config) {
      remove(KEY_USERNAME)
      remove(KEY_PASSWORD)
      save()
    }

    showLoginScreen("Log in as another user")
  }

  companion object {
    val KEY_USERNAME = "username"
    val KEY_PASSWORD = "password"
  }
}

class LoginScreen : View() {
  override val root = GridPane()
  val loginController: LoginController by inject()

  var username: TextField by singleAssign()
  var password: PasswordField by singleAssign()
  var remember: CheckBox by singleAssign()

  init {
    title = "Please log in"

    with(root) {
      addClass(Styles.loginScreen)

      row("Username") {
        username = textfield()
      }

      row("Password") {
        password = passwordfield()
      }

      row("Remember me") {
        remember = checkbox()
      }

      row {
        button("Login") {
          isDefaultButton = true

          setOnAction {
            loginController.tryLogin(
              username.text,
              password.text,
              remember.isSelected
            )
          }
        }
      }

    }
  }

  fun clear() {
    username.clear()
    password.clear()
    remember.isSelected = false
  }

  fun shakeStage() {
    var x = 0
    var y = 0
    val cycleCount = 10
    val move = 10
    val keyframeDuration = Duration.seconds(0.04)

    val stage = FX.primaryStage

    val timelineX = Timeline(KeyFrame(keyframeDuration, EventHandler {
      if (x == 0) {
        stage.x = stage.x + move
        x = 1
      } else {
        stage.x = stage.x - move
        x = 0
      }
    }))

    timelineX.cycleCount = cycleCount
    timelineX.isAutoReverse = false

    val timelineY = Timeline(KeyFrame(keyframeDuration, EventHandler {
      if (y == 0) {
        stage.y = stage.y + move
        y = 1;
      } else {
        stage.y = stage.y - move
        y = 0;
      }
    }))

    timelineY.cycleCount = cycleCount;
    timelineY.isAutoReverse = false;

    timelineX.play()
    timelineY.play();
  }
}


class Styles : Stylesheet() {
  companion object {
    val loginScreen by cssclass()
  }

  init {
    select(loginScreen) {
      padding = box(15.px)
      vgap = 7.px
      hgap = 10.px
    }
  }
}


class Workbench : View() {
  override val root = BorderPane()
  val loginController: LoginController by inject()

  init {
    title = "Secure Workbench"

    with(root) {
      setPrefSize(800.0, 600.0)

      top {
        label(title) {
          font = Font.font(22.0)
        }
      }

      center {

        label("If you can see this, you are successfully logged in!")

        hbox {

          button("Logout") {
            setOnAction {
              loginController.logout()
            }
          }

          button("Exit") {
            setOnAction {
              Platform.exit()
            }
          }
        }
      }
    }
  }
}