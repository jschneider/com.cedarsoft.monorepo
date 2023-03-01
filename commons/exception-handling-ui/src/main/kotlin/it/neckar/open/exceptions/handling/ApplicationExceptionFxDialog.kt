/**
 * Copyright (C) cedarsoft GmbH.
 *
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package it.neckar.open.exceptions.handling

import it.neckar.open.exceptions.ApplicationException
import it.neckar.open.i18n.DefaultI18nConfiguration
import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Shows an application exception
 */
class ApplicationExceptionFxDialog(
  e: ApplicationException
) : Alert(AlertType.ERROR) {

  init {
    alertType = AlertType.WARNING

    title = e.getTitle() + " (" + e.errorCode + ")"
    headerText = e.getTitle() + " (" + e.errorCode + ")"
    contentText = e.getLocalizedMessage()
    isResizable = true

    dialogPane.prefWidth = 500.0

    val sw = StringWriter()
    val pw = PrintWriter(sw)
    e.printStackTrace(pw)
    val exceptionText = sw.toString()

    val label = Label("Exception Stacktrace:")

    val textArea = TextArea(exceptionText)
    textArea.isEditable = false
    textArea.isWrapText = false

    textArea.maxWidth = java.lang.Double.MAX_VALUE
    textArea.maxHeight = java.lang.Double.MAX_VALUE
    GridPane.setVgrow(textArea, Priority.ALWAYS)
    GridPane.setHgrow(textArea, Priority.ALWAYS)

    val expContent = GridPane()
    expContent.maxWidth = java.lang.Double.MAX_VALUE
    expContent.add(label, 0, 0)
    expContent.add(textArea, 0, 1)

    // Set expandable Exception into the dialog pane.
    dialogPane.expandableContent = expContent


    dialogPane.expandedProperty().addListener { _, _, _ ->
      Platform.runLater {
        dialogPane.scene.window.sizeToScene()
      }
    }
  }
}
