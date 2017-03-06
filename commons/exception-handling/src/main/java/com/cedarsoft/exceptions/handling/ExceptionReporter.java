package com.cedarsoft.exceptions.handling;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.version.Version;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.Map;

/**
 * Reports exceptions to a bug tracker
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@FunctionalInterface
public interface ExceptionReporter {
  /**
   * Reports the exception
   */
  @UiThread
  void report(@Nonnull Version applicationVersion, @Nonnull Throwable throwable);
}
