package com.cedarsoft.annotations.instrumentation.test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RunnerClass {
  @CustomThreadAnnotation
  public void methodCustomThread() {
    System.out.println("--> custom thread");
  }

  @UiThread
  public void methodOnlyFromUi() {
    System.out.println( "--> only from UI" );
  }

  @NonUiThread
  public void methodOnlyFromNonUi() {
    System.out.println( "--> only from Non UI" );
  }

  @Nonnull
  public String nonNullMethod() {
    return null;
  }

  @Nullable
  public String nullMethod(@Nonnull String param, @Nullable String param2) {
    return null;
  }
}
