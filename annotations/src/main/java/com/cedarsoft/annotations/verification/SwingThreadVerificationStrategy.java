package com.cedarsoft.annotations.verification;

import javax.swing.SwingUtilities;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SwingThreadVerificationStrategy implements UiThreadVerificationStrategy {
  @Override
  public boolean isUiThread() {
    return SwingUtilities.isEventDispatchThread();
  }
}
