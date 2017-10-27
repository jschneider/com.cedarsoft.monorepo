package com.cedarsoft.exceptions.handling.notification;

import com.cedarsoft.exceptions.handling.Messages;
import com.cedarsoft.unit.other.px;
import com.jidesoft.utils.SystemInfo;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import org.tbee.javafx.scene.layout.MigPane;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class FxBalloonDialog extends Dialog<ButtonType> {

  @Nonnull
  private final Notification notification;

  public FxBalloonDialog(@Nonnull Notification notification) {
    this.notification = notification;

    getDialogPane().getStylesheets().add(getClass().getResource("FxBalloonDialog.css").toExternalForm());

    setResizable(true);

    MigPane migPane = new MigPane("fill, wrap 2, insets 0", "[][grow,fill]", "[fill][fill,grow][fill]");
    getDialogPane().getChildren().add(migPane);

    ImageView imageView = new ImageView();
    imageView.getStyleClass().add("icon");
    migPane.add(imageView);

    Text headline = new Text(notification.getTitle());
    headline.getStyleClass().add("headline");
    //migPane.getChildren().add(headline);

    Text message = new Text(notification.getMessage());
    message.getStyleClass().add("message");
    migPane.add(message, "span");

    @Nullable DetailsCallback detailsCallback = notification.getDetailsCallback();
    if (detailsCallback != null) {
      Hyperlink closeLink = new Hyperlink(Messages.get("details"));
      closeLink.getStyleClass().add("details-link");
      migPane.add(closeLink, "alignx right, span");

      closeLink.onActionProperty().setValue(event -> {
        closeBalloon();
        detailsCallback.detailsClicked(notification);
      });
    }

    {
      //Close icon
      FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.TIMES_CIRCLE_ALT);
      iconView.getStyleClass().add("close-icon");

      iconView.setManaged(false);
      iconView.setGlyphSize(18);
      migPane.getChildren().add(iconView);

      iconView.setY(0);
      iconView.setTextOrigin(VPos.TOP);
      @px double closeIconWidth = iconView.getBoundsInLocal().getWidth();
      iconView.xProperty().bind(Bindings.createDoubleBinding(() -> getDialogPane().getWidth() - closeIconWidth, getDialogPane().widthProperty()));

      iconView.onMouseClickedProperty().setValue(event -> closeBalloon());
    }

    initStyle(StageStyle.UNDECORATED);

    migPane.getStyleClass().add("pane");
    getDialogPane().getStyleClass().add("balloon-dialog");

    //Workaround to fix size
    if (SystemInfo.isLinux()) {
      Platform.runLater(() -> {
        Bounds boundsInLocal = getDialogPane().getBoundsInLocal();
        getDialogPane().getScene().getWindow().setWidth(boundsInLocal.getWidth() + 30);
        getDialogPane().getScene().getWindow().setHeight(boundsInLocal.getHeight() + 30);
      });
    }
  }

  @Nonnull
  public Notification getNotification() {
    return notification;
  }

  public abstract void closeBalloon();
}
