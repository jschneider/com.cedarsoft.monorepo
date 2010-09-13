package com.cedarsoft.codegen.model.test;

/**
 *
 */
public class BoolGetter {
  private final boolean dependent;

  public BoolGetter( boolean dependent ) {
    this.dependent = dependent;
  }

  public boolean isDependent() {
    return dependent;
  }
}
