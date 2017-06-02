package com.cedarsoft.commons.test;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MyCustomControlController extends VBox {
  @FXML
  private TextField textField;

  public MyCustomControlController() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyCustomControl.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    try {
      fxmlLoader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getText() {
    return textProperty().get();
  }

  public void setText(String value) {
    textProperty().set(value);
  }

  @Nonnull
  public StringProperty textProperty() {
    return textField.textProperty();
  }

  @FXML
  public void handleButtonAction(@Nonnull ActionEvent actionEvent) {
    System.out.println("button clicked");
  }
}
