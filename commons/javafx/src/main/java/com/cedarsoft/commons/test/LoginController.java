package com.cedarsoft.commons.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class LoginController {
  @FXML
  private Text actionTarget;
  @FXML
  private TextField userNameField;
  @FXML
  private PasswordField passwordField;

  @FXML
  public void handleLoginButtonAction(@Nonnull ActionEvent actionEvent) {
    System.out.println("Login <" + userNameField.getText() + "> with Pass <" + passwordField.getText() + ">");
    actionTarget.setText("Log in failed for <" + userNameField.getText() + ">");
  }
}
