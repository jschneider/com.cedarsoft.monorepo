package com.cedarsoft.annotations.instrumentation.test;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RunnerClass {
  @UiThread
  public void methodOnlyFromUi() {
    System.out.println( "--> only from UI" );
  }

  @NonUiThread
  public void methodOnlyFromNonUi() {
    System.out.println( "--> only from Non UI" );
  }

  @Nonnull
  public String nonNullMethod(@Nonnull String param) {
    return null;
  }

  @Nullable
  public String nullMethod() {
    return null;
  }
}
