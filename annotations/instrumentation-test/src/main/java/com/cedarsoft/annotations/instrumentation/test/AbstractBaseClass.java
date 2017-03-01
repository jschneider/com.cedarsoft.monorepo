package com.cedarsoft.annotations.instrumentation.test;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractBaseClass {
  @Nonnull
  private String inAbstrBaseClass = "asdf";

  @Nonnull
  public String getInAbstrBaseClass() {
    return inAbstrBaseClass;
  }

  public void setInAbstrBaseClass(@Nonnull String inAbstrBaseClass) {
    this.inAbstrBaseClass = inAbstrBaseClass;
  }
}
