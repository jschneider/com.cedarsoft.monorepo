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
package com.cedarsoft.exceptions.handling

import it.neckar.open.annotations.UiThread
import com.cedarsoft.swing.common.Borders
import com.cedarsoft.swing.common.dialog.AbstractDialog
import com.cedarsoft.unit.other.px
import com.cedarsoft.version.Version
import com.jidesoft.dialog.BannerPanel
import com.jidesoft.dialog.ButtonPanel
import net.miginfocom.swing.MigLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.JComponent
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder
import javax.swing.text.JTextComponent

/**
 * Showing an internal exception
 */
class InternalExceptionDialog(
  parent: JFrame?,
  val throwable: Throwable,
  val exceptionReporter: ExceptionReporter?,
  val applicationVersion: Version
) : AbstractDialog(parent) {

  @UiThread
  private lateinit var stackTraceScrollPane: JScrollPane

  @px
  private var collapsedHeight: Int = 0

  override fun createBannerPanel(): JComponent {
    val bannerPanel = BannerPanel(Messages.get("internal.exception.message"), null, AbstractDialog.Icons.ERROR)
    title = Messages.get("internal.exception.message")
    return bannerPanel
  }

  override fun createContentPanel(): JComponent {
    val panel = object : JPanel(MigLayout("wrap 1, insets 0, hidemode 3", "[fill, grow, 600::]", "[fill][fill, grow]")) {
      override fun getPreferredSize(): Dimension {
        val preferredSize = super.getPreferredSize()
        preferredSize.width = Math.max(preferredSize.width, MAX_WIDTH)
        return preferredSize
      }
    }

    val errorMessage = JEditorPane()
    errorMessage.isEditable = false
    errorMessage.text = createErrorMessageText()
    errorMessage.border = Borders.MESSAGE_BORDER

    val errorScrollPane = object : JScrollPane(errorMessage) {
      override fun getMinimumSize(): Dimension {
        return super.getPreferredSize()
      }
    }
    errorScrollPane.border = EmptyBorder(0, 0, 0, 0)
    panel.add(errorScrollPane)

    val stackTraceField = JEditorPane()
    stackTraceField.isEditable = false
    stackTraceField.contentType = "text/html"
    stackTraceField.caretPosition = 0
    stackTraceField.text = createDetailsText()

    stackTraceScrollPane = JScrollPane(stackTraceField)
    stackTraceScrollPane.isVisible = false
    panel.add(stackTraceScrollPane)

    return panel
  }

  override fun createButtonPanel(): ButtonPanel? {
    val otherActions = ArrayList<Action>()

    if (exceptionReporter != null) {
      otherActions.add(object : AbstractAction(Messages.get("report.error.action")) {
        override fun actionPerformed(e: ActionEvent) {
          //disable button, ensure can only report once
          isEnabled = false
          exceptionReporter.report(throwable)
        }
      })
    }

    otherActions.add(object : AbstractAction(Messages.get("details") + " >>") {
      override fun actionPerformed(e: ActionEvent) {
        if (stackTraceScrollPane == null) {
          throw IllegalStateException("stackTraceScrollPane not found")
        }

        if (stackTraceScrollPane.isVisible) {
          stackTraceScrollPane.isVisible = false
          setSize(width, collapsedHeight)
          putValue(Action.NAME, Messages.get("details") + " >>")
        } else {
          collapsedHeight = height
          stackTraceScrollPane.isVisible = true
          //fix scroll position
          (stackTraceScrollPane.viewport.view as JTextComponent).caretPosition = 0
          setSize(width, height + 250)
          putValue(Action.NAME, Messages.get("details") + " <<")
        }

        stackTraceScrollPane.parent.revalidate()
      }
    })


    return createButtonPanel(object : AbstractAction(Messages.get("close")) {
      override fun actionPerformed(e: ActionEvent) {
        ok()
      }
    }, null, *otherActions.toTypedArray())
  }

  private fun createDetailsText(): String {
    val html = StringBuilder("<html>")

    var ex: Throwable? = this.throwable
    while (ex != null) {
      html.append("<h3>").append(ex.message).append("</h3>")
      html.append("<h4>").append(ex.javaClass.name).append("</h4>")
      html.append("<pre>")
      for (el in ex.stackTrace) {
        html.append("    ").append(el.toString().replace("<init>", "&lt;init&gt;")).append("\n")
      }
      html.append("</pre>")
      ex = ex.cause
    }
    html.append("</html>")

    return html.toString()
  }

  private fun createErrorMessageText(): String {
    return throwable.javaClass.name + "\n" + throwable.localizedMessage
  }

  companion object {
    @px
    val MAX_WIDTH = 500
  }

}
