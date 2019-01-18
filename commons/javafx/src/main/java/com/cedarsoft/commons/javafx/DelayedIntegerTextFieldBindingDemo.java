package com.cedarsoft.commons.javafx;

import static com.cedarsoft.commons.javafx.Components.label;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DelayedIntegerTextFieldBindingDemo extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    IntegerProperty integerProperty = new SimpleIntegerProperty(8000);

    VBox vBox = new VBox(10);
    vBox.setPadding(new Insets(20));

    vBox.getChildren().add(label(Bindings.createStringBinding(() -> Integer.toString(integerProperty.get()), integerProperty)));

    Button randomButton = new Button("Random!");
    randomButton.setOnAction(evt -> integerProperty.set((int) (8000 * Math.random())));
    vBox.getChildren().add(randomButton);

    HBox hBox1 = new HBox(10);
    hBox1.getChildren().add(label("Delayed"));
    hBox1.getChildren().add(Components.textFieldIntegerDelayed(integerProperty));
    vBox.getChildren().add(hBox1);

    HBox hBox2 = new HBox(10);
    hBox2.getChildren().add(label("Delayed, only positive"));
    hBox2.getChildren().add(Components.textFieldIntegerDelayed(integerProperty, i -> i >= 0));
    vBox.getChildren().add(hBox2);

    HBox hBox3 = new HBox(10);
    hBox3.getChildren().add(label("Delayed, only even"));
    hBox3.getChildren().add(Components.textFieldIntegerDelayed(integerProperty, i -> i % 2 == 0));
    vBox.getChildren().add(hBox3);

    HBox hBox4 = new HBox(40);
    hBox4.getChildren().add(label("Focus catcher"));
    hBox4.getChildren().add(new TextField());
    vBox.getChildren().add(hBox4);

    primaryStage.setScene(new Scene(vBox));

    primaryStage.show();

  }
}
