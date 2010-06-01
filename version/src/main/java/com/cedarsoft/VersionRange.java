/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

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
    if ( max.smallerThan( min ) ) {
      throw new IllegalArgumentException( "Max <" + max + "> is smaller than min <" + min + ">" );
    }

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

  /**
   * Returns true if this range contains the other range completely
   *
   * @param other the other range
   * @return true if this contains the other range completely, false otherwise
   */
  public boolean containsCompletely( @NotNull VersionRange other ) {
    //Verify the lower border
    final boolean lower;
    if ( includeLower ) {
      lower = getMin().sameOrSmallerThan( other.getMin() );
    } else {
      lower = getMin().smallerThan( other.getMin() );
    }

    if ( !lower ) {
      return false;
    }

    //Verify the upper border
    if ( includeUpper ) {
      return getMax().sameOrGreaterThan( other.getMax() );
    } else {
      return getMax().greaterThan( other.getMax() );
    }
  }

  public boolean contains( @NotNull Version version ) {
    if ( includeLower ) {
      if ( !version.sameOrGreaterThan( min ) ) {
        return false;
      }
    } else {
      if ( !version.greaterThan( min ) ) {
        return false;
      }
    }

    if ( includeUpper ) {
      return version.sameOrSmallerThan( max );
    } else {
      return version.smallerThan( max );
    }
  }

  public boolean overlaps( @NotNull VersionRange other ) {
    boolean lower;
    if ( includeLower && other.includeUpper ) {
      lower = getMin().sameOrSmallerThan( other.getMax() );
    } else {
      lower = getMin().smallerThan( other.getMax() );
    }

    boolean upper;
    if ( includeUpper && other.includeLower ) {
      upper = getMax().sameOrGreaterThan( other.getMin() );
    } else {
      upper = getMax().greaterThan( other.getMin() );
    }

    return lower && upper;
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

  @NotNull
  public static Factory from( @NotNull Version min ) {
    return new Factory( min );
  }

  @NotNull
  public static Factory from( int major, int minor, int build ) {
    return new Factory( new Version( major, minor, build ) );
  }

  @NotNull
  public VersionRange single( int major, int minor, int build ) {
    return new VersionRange( Version.valueOf( major, minor, build ), Version.valueOf( major, minor, build ) );
  }

  public static class Factory {
    @NotNull
    private final Version min;

    public Factory( @NotNull Version min ) {
      this.min = min;
    }

    @NotNull
    public VersionRange to( @NotNull Version max ) {
      return new VersionRange( min, max );
    }

    @NotNull
    public VersionRange to( int major, int minor, int build ) {
      return to( new Version( major, minor, build ) );
    }

    @NotNull
    public VersionRange to() {
      return single();
    }

    @NotNull
    public VersionRange single() {
      return new VersionRange( min, min );
    }
  }
}
