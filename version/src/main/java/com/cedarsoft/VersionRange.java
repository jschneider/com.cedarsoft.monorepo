package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 *
 */
public class VersionRange implements Serializable {
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

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof VersionRange ) ) return false;

    VersionRange range = ( VersionRange ) o;

    if ( !max.equals( range.max ) ) return false;
    if ( !min.equals( range.min ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = min.hashCode();
    result = 31 * result + max.hashCode();
    return result;
  }

  @Override
  @NotNull
  @NonNls
  public String toString() {
    return min + "-" + max;
  }
}
