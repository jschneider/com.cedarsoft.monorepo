package com.cedarsoft.exceptions.handling;

import org.junit.*;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationExceptionDialogDemo {
  @Before
  public void setUp() throws Exception {
    UIManager.setLookAndFeel(new NimbusLookAndFeel());
  }

  @Test
  public void basics() throws Exception {
    MyTestException e = new MyTestException(MyTestException.TestExceptionDetails.ERROR_1);
    ApplicationExceptionDialog dialog = new ApplicationExceptionDialog(null, e, null);

    dialog.setVisible(true);
  }
}
