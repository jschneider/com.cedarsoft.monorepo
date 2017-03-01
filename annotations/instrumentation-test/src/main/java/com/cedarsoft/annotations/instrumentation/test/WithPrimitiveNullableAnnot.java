package com.cedarsoft.annotations.instrumentation.test;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 * @noinspection NullableProblems
 */
public class WithPrimitiveNullableAnnot {
  @Nonnull
  private int a;

  public WithPrimitiveNullableAnnot( @Nonnull int a ) {
    this.a = a;
  }

  @Nonnull
  public int getA() {
    return a;
  }

  public void setA( @Nonnull int a ) {
    this.a = a;
  }
}
