package com.cedarsoft.annotations.verification;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface UiThreadVerificationStrategy {
  /**
   * Returns true if the current thread is the ui thread
   *
   * @return true if the current thread is the ui thread, false otherwise
   */
  boolean isUiThread();
}
