/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.exceptions.handling;

import com.cedarsoft.exceptions.ApplicationException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import javax.annotation.Nonnull;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Shows an application exception
 */
public class ApplicationExceptionFxDialog extends Alert {
  @Nonnull
  private final ApplicationException e;

  public ApplicationExceptionFxDialog(@Nonnull ApplicationException e) {
    super(AlertType.ERROR);
    this.e = e;

    setTitle(e.getErrorCode().toString());
    setHeaderText(e.getTitle() + " (" + e.getErrorCode() + ")");

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    String exceptionText = sw.toString();

    Label label = new Label("Exception Stacktrace:");

    TextArea textArea = new TextArea(exceptionText);
    textArea.setEditable(false);
    textArea.setWrapText(false);

    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    GridPane expContent = new GridPane();
    expContent.setMaxWidth(Double.MAX_VALUE);
    expContent.add(label, 0, 0);
    expContent.add(textArea, 0, 1);

    // Set expandable Exception into the dialog pane.
    getDialogPane().setExpandableContent(expContent);


    getDialogPane().expandedProperty().addListener((observable, oldValue, newValue) -> {
      Platform.runLater(() -> {
        System.out.println("adjust size");
        getDialogPane().getScene().getWindow().sizeToScene();
      });
    });
  }
}
