package com.cedarsoft.exceptions.handling;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ReportingExceptionsDialogDemo {
  @Test
  public void name() throws Exception {
    new ReportingExceptionsDialog(null).setVisible(true);
  }
}
