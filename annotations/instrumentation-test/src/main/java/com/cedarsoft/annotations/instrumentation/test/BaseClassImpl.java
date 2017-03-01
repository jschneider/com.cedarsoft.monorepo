package com.cedarsoft.annotations.instrumentation.test;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class BaseClassImpl extends BaseClass {
  @Nonnull
  private final Object o;

  public BaseClassImpl(@Nonnull String asdf, @Nonnull Object o) {
    super(asdf);
    this.o = o;

    assert "asdf" != null;
  }

  @Nonnull
  public Object getO() {
    return o;
  }
}
