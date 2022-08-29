/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.commons.test

import com.cedarsoft.common.resources.getResourceSafe
import javafx.application.Application
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.text.Text
import javafx.stage.Stage
import net.miginfocom.layout.LC
import org.tbee.javafx.scene.layout.MigPane

/**
 */
class JavaFxRunner : Application() {
  @Throws(Exception::class)
  override fun start(primaryStage: Stage) {
    println("Thread.currentThread().getName() = " + Thread.currentThread().name)
    println("Platform.isFxApplicationThread() = " + Platform.isFxApplicationThread())
    primaryStage.title = "Da Title"
    val headLine = Text("Login")
    headLine.id = "headLine"
    val userNameLabel = Label("User Name")
    val passwordLabel = Label("Password")
    val userTextField = TextField()
    val passwordField = PasswordField()
    val actionTarget = Text()
    actionTarget.id = "actionTarget"
    val loginButton = Button("Login")
    loginButton.onAction = EventHandler { event: ActionEvent? -> actionTarget.text = "Login failed - check user name and password" }

    //StackPane root = new StackPane();
    //root.getChildren().addAll(button);
    //root.getChildren().addAll(new Button("S"));
    val layoutConstraints = LC()
    layoutConstraints.alignX("center")
    val pane = MigPane("wrap 2, align 50% 50%", "[][fill]", "")
    //GridPane pane = new GridPane();
    //pane.setAlignment(Pos.CENTER);
    //pane.setHgap(10);
    //pane.setVgap(10);
    pane.padding = Insets(25.0, 25.0, 25.0, 25.0)
    pane.add(headLine, "span")
    pane.add(userNameLabel, "")
    pane.add(userTextField, "")
    pane.add(passwordLabel, "")
    pane.add(passwordField, "")
    pane.add(loginButton, "span, alignx right")
    pane.add(actionTarget, "span")
    val scene = Scene(pane, 1920.0, 1080.0)
    primaryStage.scene = scene
    scene.stylesheets.add(javaClass.getResourceSafe("JavaFxRunner.css").toExternalForm())
    primaryStage.show()
  }

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      launch(*args)
    }
  }
}
