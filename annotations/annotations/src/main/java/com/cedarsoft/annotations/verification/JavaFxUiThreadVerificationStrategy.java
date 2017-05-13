package com.cedarsoft.annotations.verification;

import javafx.application.Platform;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Deprecated
public class JavaFxUiThreadVerificationStrategy implements UiThreadVerificationStrategy {
  @Override
  public boolean isUiThread() {
    return Platform.isFxApplicationThread();
  }
}
