package com.cedarsoft.annotations.instrumentation.test2;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ObjectWithNull {
  @Nonnull
  private final String s;

  public ObjectWithNull( @Nonnull String s ) {
    this.s = s;
  }

  @Nonnull
  public String getS() {
    return null;
  }
}
