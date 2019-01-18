package com.cedarsoft.commons.javafx;

import static com.cedarsoft.commons.javafx.Components.hbox5;
import static com.cedarsoft.commons.javafx.Components.label;
import static com.cedarsoft.commons.javafx.Components.textField;
import static com.cedarsoft.commons.javafx.Components.textFieldDelayed;
import static com.cedarsoft.commons.javafx.Components.vbox5;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DelayedTextFieldBindingDemo extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    StringProperty property = new SimpleStringProperty("da content");

    primaryStage.setScene(new Scene(vbox5(
      label(property),
      hbox5(label("Normal"), textField(property)),
      hbox5(label("Delayed 1"), textFieldDelayed(property)),
      hbox5(label("Delayed 2"), textFieldDelayed(property))
    ), 800, 600));

    primaryStage.show();

  }
}
