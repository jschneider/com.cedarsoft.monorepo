package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import com.cedarsoft.unit.other.px;

import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

/**
 * A popup that displays an information message.
 *
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class InfoPopup extends Popup {
  @px
  public static final double DEFAULT_MAX_WIDTH = 500.0;

  private InfoPopup(@Nonnull Node content, double maxWidth) {
    setAutoHide(true);
    setAutoFix(true);

    Parent root = createContent(content, maxWidth);

    root.getStylesheets().add(getClass().getResource("InfoPopupService.css").toExternalForm());
    root.getStyleClass().add("sick-uiglv2");
    root.getStyleClass().add("info-popup");

    getContent().add(root);
  }

  private void show(@Nonnull Node parent) {
    Bounds bounds = parent.localToScreen(parent.getLayoutBounds());
    show(parent, bounds.getMinX() + 5, bounds.getMinY() + bounds.getHeight() + 5);
  }

  @Nonnull
  private static Parent createContent(@Nonnull Node content, double maxWidth) {
    VBox root = new VBox(content);
    root.setMaxWidth(maxWidth);
    return root;
  }

  private static void showInfoPopup(@Nonnull Node parent, @Nonnull Node content, double maxWidth) {
    new InfoPopup(content, maxWidth).show(parent);
  }

  @Nonnull
  public static ImageView createInfoBox(@Nonnull String message) {
    return createInfoBox(message, DEFAULT_MAX_WIDTH);
  }

  @Nonnull
  public static ImageView createInfoBox(@Nonnull String message, double maxWidth) {
    Text text = new Text(message);
    TextFlow textFlow = new TextFlow(text);
    return createInfoBox(textFlow, maxWidth);
  }

  @Nonnull
  public static ImageView createInfoBox(@Nonnull Node content) {
    return createInfoBox(content, DEFAULT_MAX_WIDTH);
  }

  @Nonnull
  public static ImageView createInfoBox(@Nonnull Node content, double maxWidth) {
    ImageView imageView = new ImageView(InfoPopup.class.getResource("info_16.png").toExternalForm());
    imageView.setCursor(Cursor.HAND);

    imageView.setOnMouseClicked(event -> showInfoPopup(imageView, content, maxWidth));

    return imageView;
  }
}
