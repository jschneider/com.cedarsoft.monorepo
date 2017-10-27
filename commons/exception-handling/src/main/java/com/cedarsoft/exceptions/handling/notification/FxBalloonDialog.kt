package com.cedarsoft.exceptions.handling.notification

import com.cedarsoft.exceptions.handling.Messages
import com.cedarsoft.unit.other.px
import com.jidesoft.utils.SystemInfo
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.geometry.VPos
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.Hyperlink
import javafx.scene.image.ImageView
import javafx.scene.text.Text
import javafx.stage.StageStyle
import org.tbee.javafx.scene.layout.MigPane
import java.util.concurrent.Callable

/**
 * Java fx based notification balloon
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
abstract class FxBalloonDialog(notification: Notification) : Dialog<ButtonType>() {
  init {
    isResizable = true
    initStyle(StageStyle.UNDECORATED)

    dialogPane.stylesheets.add(javaClass.getResource("FxBalloonDialog.css").toExternalForm())
    dialogPane.styleClass.add("balloon-dialog")

    val migPane = MigPane("fill, wrap 2, insets 0, debug", "[][grow,fill]", "[fill][fill,grow][fill]")
    migPane.styleClass.add("pane")
    dialogPane.children.add(migPane)

    //Icon
    val imageView = ImageView()
    imageView.styleClass.add("icon")
    migPane.add(imageView)

    //Headline
    val headline = Text(notification.title)
    headline.styleClass.add("headline")
    migPane.getChildren().add(headline);

    //Message
    val message = Text(notification.message)
    message.styleClass.add("message")
    migPane.add(message, "span")

    //Details link
    val detailsCallback = notification.detailsCallback
    if (detailsCallback != null) {
      val closeLink = Hyperlink(Messages.get("details"))
      closeLink.styleClass.add("details-link")
      migPane.add(closeLink, "alignx right, span")

      closeLink.onAction = EventHandler {
        closeBalloon()
        detailsCallback.detailsClicked(notification)
      }
    }

    //Close icon
    run {
      val iconView = FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE_ALT)
      iconView.styleClass.add("close-icon")

      iconView.isManaged = false
      iconView.glyphSize = 18
      migPane.children.add(iconView)

      iconView.y = 0.0
      iconView.textOrigin = VPos.TOP
      @px val closeIconWidth = iconView.boundsInLocal.width
      iconView.xProperty().bind(Bindings.createDoubleBinding(Callable { dialogPane.width - closeIconWidth }, dialogPane.widthProperty()))

      iconView.onMouseClickedProperty().setValue(EventHandler { closeBalloon() })
    }

    //Workaround to fix size
    if (SystemInfo.isLinux()) {
      Platform.runLater {
        val boundsInLocal = dialogPane.boundsInLocal
        dialogPane.scene.window.width = boundsInLocal.width + 15
        dialogPane.scene.window.height = boundsInLocal.height + 15
      }
    }
  }

  /**
   * Is called when the balloon should be closed
   */
  abstract fun closeBalloon()
}
