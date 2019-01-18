package com.cedarsoft.commons.javafx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdvancedTextFieldDemo extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    StringProperty property = new SimpleStringProperty();

    AdvancedTextField advancedTextField = new AdvancedTextField();
    advancedTextField.bindTextBidirectional(property);

    advancedTextField.getTextField().textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.isEmpty()) {
          return;
        }

        System.out.println("Text changed from <" + oldValue + "> to <" + newValue + ">");

        try {
          int parsedValue = Integer.parseInt(newValue);
          if (parsedValue < 0) {
            advancedTextField.getTextField().setText("0");
          }
          if (parsedValue > 255) {
            advancedTextField.getTextField().setText("255");
          }
        } catch (NumberFormatException e) {
          System.out.println("COuld not parse");
          advancedTextField.getTextField().setText(oldValue);
        }
      }
    });


    AdvancedTextField advancedTextField2 = new AdvancedTextField();
    advancedTextField2.addCommitHandler((advancedTextField1, text) -> property.setValue(text));

    TextField textField = new TextField();
    textField.textProperty().bindBidirectional(property);

    Scene scene = new Scene(Components.vbox5(textField, advancedTextField, advancedTextField2), 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();


    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {
      int counter;

      @Override
      public void handle(ActionEvent event) {
        counter++;
        property.set(String.valueOf(counter));
      }
    }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}
