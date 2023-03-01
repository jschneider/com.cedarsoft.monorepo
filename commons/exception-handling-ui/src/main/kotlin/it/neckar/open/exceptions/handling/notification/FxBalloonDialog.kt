package it.neckar.open.exceptions.handling.notification

import it.neckar.open.resources.getResourceSafe
import it.neckar.open.exceptions.handling.Messages
import it.neckar.open.unit.other.px
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.geometry.VPos
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.DialogPane
import javafx.scene.control.Hyperlink
import javafx.scene.image.ImageView
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.StageStyle
import org.tbee.javafx.scene.layout.MigPane
import java.util.concurrent.Callable

/**
 * Java fx based notification balloon
 */
abstract class FxBalloonDialog(notification: Notification) : Dialog<ButtonType>() {
  init {
    initModality(Modality.NONE)
    initStyle(StageStyle.UNDECORATED)
    isResizable = true

    //Add a custom dialog pane to remove the button bar
    dialogPane = object : DialogPane() {
      override fun createButtonBar(): Node? {
        return null;
      }
    }
    dialogPane.stylesheets.add(javaClass.getResourceSafe("FxBalloonDialog.css").toExternalForm())
    dialogPane.styleClass.add("balloon-dialog")

    dialogPane.header = null

    val contentPane = MigPane("fill, wrap 2, insets 0", "[][grow,fill]", "[fill][fill,grow][fill]")
    contentPane.styleClass.add("pane")
    dialogPane.content = contentPane

    //Icon
    val imageView = ImageView()
    imageView.styleClass.add("icon")
    contentPane.add(imageView)

    //Headline
    val headline = Text(notification.title)
    headline.styleClass.add("headline")
    contentPane.getChildren().add(headline);

    contentPane.add(Text(notification.message), "span, wrap")

    //Details link
    val detailsCallback = notification.detailsCallback
    if (detailsCallback != null) {
      val detailsLink = Hyperlink(Messages.get("details"))
      detailsLink.styleClass.add("details-link")
      contentPane.add(detailsLink, "alignx right, span")

      detailsLink.layoutX = 20.0
      detailsLink.layoutY = 20.0

      detailsLink.onAction = EventHandler {
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
      contentPane.children.add(iconView)

      iconView.y = 0.0
      iconView.textOrigin = VPos.TOP
      @px val closeIconWidth = iconView.boundsInLocal.width
      iconView.xProperty().bind(Bindings.createDoubleBinding(Callable { dialogPane.width - closeIconWidth }, dialogPane.widthProperty()))

      iconView.onMouseClickedProperty().setValue(EventHandler { closeBalloon() })
    }
//
//    //Workaround to fix size
//    if (SystemInfo.isLinux()) {
//      Platform.runLater {
//        val boundsInLocal = dialogPane.boundsInLocal
//        dialogPane.scene.window.width = boundsInLocal.width
//        dialogPane.scene.window.height = boundsInLocal.height
////        dialogPane.scene.window.width = boundsInLocal.width + 15
////        dialogPane.scene.window.height = boundsInLocal.height + 15
//      }
//    }
//    dialogPane.autosize()
//    dialogPane.scene.window.sizeToScene()
  }

  /**
   * Is called when the balloon should be closed
   */
  abstract fun closeBalloon()
}
