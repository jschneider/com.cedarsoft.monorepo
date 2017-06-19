/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
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
package com.cedarsoft.exceptions.handling;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.swing.common.Borders;
import com.cedarsoft.swing.common.dialog.AbstractDialog;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.version.Version;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.dialog.ButtonPanel;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Showing an internal exception
 */
public class InternalExceptionDialog extends AbstractDialog {
  @px
  public static final int MAX_WIDTH = 500;
  @Nonnull
  private final Throwable throwable;
  @Nullable
  private final ExceptionReporter exceptionReporter;
  @Nonnull
  private final Version applicationVersion;

  @Nullable
  @UiThread
  private JScrollPane stackTraceScrollPane;

  public InternalExceptionDialog(@Nullable JFrame parent, @Nonnull Throwable throwable, @Nullable ExceptionReporter exceptionReporter, @Nonnull Version applicationVersion) {
    super(parent);
    this.throwable = throwable;
    this.exceptionReporter = exceptionReporter;
    this.applicationVersion = applicationVersion;
  }

  @Override
  public JComponent createBannerPanel() {
    BannerPanel bannerPanel = new BannerPanel(Messages.get("internal.exception.message"), null, Icons.ERROR);
    setTitle(Messages.get("internal.exception.message"));
    return bannerPanel;
  }

  @Override
  public JComponent createContentPanel() {
    JPanel panel = new JPanel(new MigLayout("wrap 1, insets 0, hidemode 3", "[fill, grow, 600::]", "[fill][fill, grow]")) {
      @Override
      public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = Math.max(preferredSize.width, MAX_WIDTH);
        return preferredSize;
      }
    };

    JEditorPane errorMessage = new JEditorPane();
    errorMessage.setEditable(false);
    errorMessage.setText(createErrorMessageText());
    errorMessage.setBorder(Borders.MESSAGE_BORDER);

    JScrollPane errorScrollPane = new JScrollPane(errorMessage) {
      @Override
      public Dimension getMinimumSize() {
        return super.getPreferredSize();
      }
    };
    errorScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    panel.add(errorScrollPane);

    JEditorPane stackTraceField = new JEditorPane();
    stackTraceField.setEditable(false);
    stackTraceField.setContentType("text/html");
    stackTraceField.setCaretPosition(0);
    stackTraceField.setText(createDetailsText());

    stackTraceScrollPane = new JScrollPane(stackTraceField);
    stackTraceScrollPane.setVisible(false);
    panel.add(stackTraceScrollPane);

    return panel;
  }

  @px
  private int collapsedHeight;

  @Nullable
  @Override
  public ButtonPanel createButtonPanel() {
    List<Action> otherActions = new ArrayList<>();

    if (exceptionReporter != null) {
      otherActions.add(new AbstractAction(Messages.get("report.error.action")) {
        @Override
        public void actionPerformed(ActionEvent e) {
          //disable button, ensure can only report once
          setEnabled(false);
          exceptionReporter.report(throwable);
        }
      });
    }

    otherActions.add(new AbstractAction(Messages.get("details") + " >>") {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (stackTraceScrollPane == null) {
          throw new IllegalStateException("stackTraceScrollPane not found");
        }

        if (stackTraceScrollPane.isVisible()) {
          stackTraceScrollPane.setVisible(false);
          setSize(getWidth(), collapsedHeight);
          putValue(Action.NAME, Messages.get("details") + " >>");
        }
        else {
          collapsedHeight = getHeight();
          stackTraceScrollPane.setVisible(true);
          //fix scroll position
          ((JTextComponent) stackTraceScrollPane.getViewport().getView()).setCaretPosition(0);
          setSize(getWidth(), getHeight() + 250);
          putValue(Action.NAME, Messages.get("details") + " <<");
        }

        stackTraceScrollPane.getParent().revalidate();
      }
    });


    return createButtonPanel(new AbstractAction(Messages.get("close")) {
      @Override
      public void actionPerformed(ActionEvent e) {
        ok();
      }
    }, null, otherActions.toArray(new Action[0]));
  }

  @Nonnull
  private String createDetailsText() {
    StringBuilder html = new StringBuilder("<html>");

    Throwable ex = this.throwable;
    while (ex != null) {
      html.append("<h3>").append(ex.getMessage()).append("</h3>");
      html.append("<h4>").append(ex.getClass().getName()).append("</h4>");
      html.append("<pre>");
      for (StackTraceElement el : ex.getStackTrace()) {
        html.append("    ").append(el.toString().replace("<init>", "&lt;init&gt;")).append("\n");
      }
      html.append("</pre>");
      ex = ex.getCause();
    }
    html.append("</html>");

    return html.toString();
  }

  @Nonnull
  private String createErrorMessageText() {
    return throwable.getClass().getName() + "\n" + throwable.getLocalizedMessage();
  }

}
