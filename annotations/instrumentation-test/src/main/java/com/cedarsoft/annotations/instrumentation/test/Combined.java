package com.cedarsoft.annotations.instrumentation.test;

import javax.annotation.Nonnull;

import com.cedarsoft.annotations.NonUiThread;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Combined {
  @Nonnull
  @NonUiThread
  public String doIt() {
    System.out.println("com.cedarsoft.annotations.instrumentation.test.Combined.doIt");
    return null;
  }
}
