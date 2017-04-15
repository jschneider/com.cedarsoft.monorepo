package com.cedarsoft.swing.common.dialog;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.junit.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class OptionDialogDemo {
  @Before
  public void setUp() throws Exception {
    UIManager.setLookAndFeel(new NimbusLookAndFeel());
  }

  @Test
  public void confirm() throws Exception {
    OptionDialog.showConfirmDialog(null, "Really?", "daTitle");
  }

  @Test
  public void message() throws Exception {
    OptionDialog.showMessageDialog(null, "Really?", "daTitle");
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
}
