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

  private final boolean includeLower;
  private final boolean includeUpper;

  public VersionRange( @NotNull Version min, @NotNull Version max ) {
    this( min, max, true, true );
  }

  public VersionRange( @NotNull Version min, @NotNull Version max, boolean includeLower, boolean includeUpper ) {
    this.min = min;
    this.max = max;
    this.includeLower = includeLower;
    this.includeUpper = includeUpper;
  }

  @NotNull
  public Version getMin() {
    return min;
  }

  @NotNull
  public Version getMax() {
    return max;
  }

  public boolean isIncludeLower() {
    return includeLower;
  }

  public boolean isIncludeUpper() {
    return includeUpper;
  }

  public boolean contains( @NotNull Version version ) {
    if ( includeLower ) {
      if ( !version.sameOrGreaterThan( min ) ) {
        return false;
      }
    }else{
      if ( !version.greaterThan( min ) ) {
        return false;
      }
    }

    if ( includeUpper ) {
      return version.sameOrSmallerThan( max );
    }else {
      return version.smallerThan( max );
    }
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
    StringBuilder builder = new StringBuilder();

    if ( includeLower ) {
      builder.append( "[" );
      builder.append( min );
    } else {
      builder.append( "]" );
      builder.append( min );
    }

    builder.append( "-" );

    if ( includeUpper ) {
      builder.append( max );
      builder.append( "]" );
    } else {
      builder.append( max );
      builder.append( "[" );
    }

    return builder.toString();
  }
}
