package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class VersionRange {
  @NotNull
  private final Version min;
  @NotNull
  private final Version max;

  public VersionRange( @NotNull Version min, @NotNull Version max ) {
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

  public boolean contains( @NotNull Version version ) {
    return version.sameOrGreaterThan( min ) && version.sameOrSmallerThan( max );
  }
}
