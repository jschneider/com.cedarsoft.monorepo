package com.cedarsoft.annotations.instrumentation.test;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class BaseClass {
  @Nonnull
  private final String asdf;

  public BaseClass(@Nonnull String asdf) {
    this.asdf = asdf;
  }

  @Nonnull
  public String getAsdf() {
    return asdf;
  }
}
