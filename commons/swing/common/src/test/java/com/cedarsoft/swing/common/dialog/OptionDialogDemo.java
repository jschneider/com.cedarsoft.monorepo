package com.cedarsoft.swing.common.dialog;

import org.junit.*;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

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
}
