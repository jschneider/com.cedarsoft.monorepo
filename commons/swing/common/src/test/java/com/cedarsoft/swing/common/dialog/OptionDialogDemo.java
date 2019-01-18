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
package com.cedarsoft.swing.common.dialog;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.junit.jupiter.api.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class OptionDialogDemo {
  @BeforeEach
  public void setUp() throws Exception {
    UIManager.setLookAndFeel(new NimbusLookAndFeel());
  }

  @Test
  public void confirm() throws Exception {
    OptionDialog.showConfirmDialog(null, "Really?", "daTitle");
  }

  @Test
  public void message() throws Exception {
    OptionDialog.ResultType result = OptionDialog.showMessageDialog(null, "Really?", "daTitle");
    System.out.println("result = " + result);
  }

  @Test
  public void error() throws Exception {
    OptionDialog.showMessageDialog(null, "Really?", "daTitle", OptionDialog.MessageType.ERROR_MESSAGE);
  }

  @Test
  public void radio() throws Exception {
    Integer result = OptionDialog.showRadioDialog(null, "What do you want?", "daTitle", OptionDialog.MessageType.ERROR_MESSAGE, 1, "A", "B", "C");
    System.out.println("result = " + result);
  }

  @Test
  public void comboBoxSimple() throws Exception {
    DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(new String[]{"a", "b", "c", "d"});
    String result = OptionDialog.showComboBoxDialog(null, "What do you want?", "daTitle", OptionDialog.MessageType.ERROR_MESSAGE, comboBoxModel);
    System.out.println("result = " + result);
  }

  @Test
  public void comboBox() throws Exception {
    DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(new String[]{"a", "b", "c", "d"});
    DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer() {
      @Override
      public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText("Da Value: <" + value + ">");
        return this;
      }
    };
    String result = OptionDialog.showComboBoxDialog(null, "What do you want?", "daTitle", OptionDialog.MessageType.ERROR_MESSAGE, comboBoxModel, 2, defaultListCellRenderer);
    System.out.println("result = " + result);
  }

  @Test
  public void input() throws Exception {
    String result = OptionDialog.showInputDialog(null, "Da Message", "Da Titel", OptionDialog.MessageType.QUESTION_MESSAGE, null);
    System.out.println("result = " + result);
  }
}
