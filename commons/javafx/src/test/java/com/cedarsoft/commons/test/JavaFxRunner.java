package com.cedarsoft.commons.test;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JavaFxRunner extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
    System.out.println("Platform.isFxApplicationThread() = " + Platform.isFxApplicationThread());

    primaryStage.setTitle("Da Title");

    Text headLine = new Text("Login");
    headLine.setId("headLine");

    Label userNameLabel = new Label("User Name");
    Label passwordLabel = new Label("Password");

    TextField userTextField = new TextField();
    PasswordField passwordField = new PasswordField();

    final Text actionTarget = new Text();
    actionTarget.setId("actionTarget");

    Button loginButton = new Button("Login");
    loginButton.setOnAction(event -> {
      actionTarget.setText("Login failed - check user name and password");
    });

    //StackPane root = new StackPane();
    //root.getChildren().addAll(button);
    //root.getChildren().addAll(new Button("S"));


    LC layoutConstraints = new LC();
    layoutConstraints.alignX("center");

    MigPane pane = new MigPane("wrap 2, align 50% 50%", "[][fill]", "");
    //GridPane pane = new GridPane();
    //pane.setAlignment(Pos.CENTER);
    //pane.setHgap(10);
    //pane.setVgap(10);
    pane.setPadding(new Insets(25, 25, 25, 25));


    pane.add(headLine, "span");
    pane.add(userNameLabel, "");
    pane.add(userTextField, "");
    pane.add(passwordLabel, "");
    pane.add(passwordField, "");

    pane.add(loginButton, "span, alignx right");

    pane.add(actionTarget, "span");


    Scene scene = new Scene(pane, 1920, 1080);
    primaryStage.setScene(scene);

    scene.getStylesheets().add(getClass().getResource("JavaFxRunner.css").toExternalForm());

    primaryStage.show();
  }
}
