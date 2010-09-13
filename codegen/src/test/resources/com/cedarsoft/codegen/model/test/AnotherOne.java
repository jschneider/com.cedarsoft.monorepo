package com.cedarsoft.codegen.model.test;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class AnotherOne {
  private final boolean dependent;

  @NotNull
  private final Version min;
  @NotNull
  private final Version max;

  public AnotherOne( boolean dependent, @NotNull Version min, @NotNull Version max ) {
    this.dependent = dependent;
    this.min = min;
    this.max = max;
  }

  @NotNull
  public Version getMin() {
    return min;
  }

  @NotNull
  public Version getMax() {
    return max;
  }


  public boolean isDependent() {
    return dependent;
  }

  public static class Version {
  }
}
