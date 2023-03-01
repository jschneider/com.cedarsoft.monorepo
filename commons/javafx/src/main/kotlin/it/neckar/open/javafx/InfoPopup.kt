package it.neckar.open.javafx

import it.neckar.open.resources.getResourceSafe
import javafx.beans.binding.Bindings
import javafx.event.EventHandler
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.Popup
import javax.annotation.Nonnull

/**
 * A popup that displays an information message.
 */
class InfoPopup private constructor(
  /**
   * The content of the info popup
   */
  content: Node,
  /**
   * The max width of the popup
   */
  maxWidth: Double
) : Popup() {
  init {
    isAutoHide = true
    isAutoFix = true

    val root = createContent(content, maxWidth)

    root.stylesheets.add(javaClass.getResourceSafe("InfoPopupService.css").toExternalForm())

    root.styleClass.add("sick-uiglv2")
    root.styleClass.add("info-popup")
    getContent().add(root)
  }

  /**
   * Show the popup for the given parent node
   */
  private fun show(@Nonnull parent: Node) {
    val bounds = parent.localToScreen(parent.layoutBounds)
    show(parent, bounds.minX + 5, bounds.minY + bounds.height + 5)
  }

  companion object {
    /**
     * The info icon
     */
    private val infoIcon = InfoPopup::class.java.getResourceSafe("info_16.png").toExternalForm()

    /**
     * Creates the content
     */
    private fun createContent(@Nonnull content: Node, maxWidth: Double): Parent {
      val root = VBox(content)
      root.maxWidth = maxWidth
      return root
    }

    /**
     * Shows the info popup
     */
    private fun showInfoPopup(parent: Node, content: Node, maxWidth: Double) {
      InfoPopup(content, maxWidth).show(parent)
    }

    @JvmStatic
    @JvmOverloads
    fun createInfoBox(@Nonnull message: String, maxWidth: Double = 500.0): ImageView {
      val text = Text(message)
      val textFlow = TextFlow(text)
      return createInfoBox(textFlow, maxWidth)
    }


    @JvmStatic
    @JvmOverloads
    fun createInfoBox(@Nonnull content: Node, maxWidth: Double = 500.0): ImageView {
      return infoBox(maxWidth) { content }
    }

    fun infoBox(maxWidth: Double = 500.0, @Nonnull content: () -> Node): ImageView {
      val imageView = ImageView(infoIcon)
      imageView.cursor = Cursor.HAND

      // make image brighter in case the image view is disabled
      imageView.effectProperty().bind(Bindings.createObjectBinding({
        if (imageView.isDisabled) {
          val brighter = ColorAdjust()
          brighter.brightness = 0.7
          brighter
        } else {
          null
        }
      }, imageView.disabledProperty()))

      imageView.onMouseClicked = EventHandler { showInfoPopup(imageView, content(), maxWidth) }
      return imageView
    }
  }
}
