package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

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

  private InfoPopup(@Nonnull String message) {
    setAutoHide(true);
    setAutoFix(true);

    Parent root = createContent(message);

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
  private static Parent createContent(@Nonnull String message) {
    Text text = new Text(message);
    TextFlow textFlow = new TextFlow(text);
    VBox root = new VBox(textFlow);
    root.setMaxWidth(500);
    return root;
  }

  public static void showInfoPopup(@Nonnull Node parent, @Nonnull String message) {
    new InfoPopup(message).show(parent);
  }

  @Nonnull
  public static ImageView createInfoBox(@Nonnull String message) {
    ImageView imageView = new ImageView(InfoPopup.class.getResource("info_16.png").toExternalForm());
    imageView.setCursor(Cursor.HAND);

    imageView.setOnMouseClicked(event -> showInfoPopup(imageView, message));

    return imageView;
  }
}
