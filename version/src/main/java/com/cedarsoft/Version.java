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
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * A version
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Version implements Comparable<Version>, Serializable {
  /** Constant <code>MAX=99</code> */
  protected static final int MAX = 99;

  protected final int major;
  protected final int minor;
  protected final int build;
  @Nullable
  @NonNls
  protected final String suffix;


  /**
   * <p>Constructor for Version.</p>
   *
   * @param major a int.
   * @param minor a int.
   * @param build a int.
   */
  public Version( int major, int minor, int build ) {
    this( major, minor, build, null );
  }

  /**
   * Creates a version
   *
   * @param major  the major part
   * @param minor  the minor part
   * @param build  the build
   * @param suffix the suffix
   */
  public Version( int major, int minor, int build, @Nullable String suffix ) {
    this.major = major;
    this.build = build;
    this.minor = minor;
    this.suffix = suffix;

    if ( major > MAX || major < 0 ) {
      throw new IllegalArgumentException( "Invalid major <" + major + ">" );
    }
    if ( minor > MAX || minor < 0 ) {
      throw new IllegalArgumentException( "Invalid minor <" + minor + ">" );
    }
    if ( build > MAX || build < 0 ) {
      throw new IllegalArgumentException( "Invalid build <" + build + ">" );
    }
  }

  /**
   * Returns the optional suffix
   *
   * @return the suffix or null if no suffix has been set
   */
  @Nullable
  @NonNls
  public String getSuffix() {
    return suffix;
  }

  /**
   * <p>Getter for the field <code>build</code>.</p>
   *
   * @return a int.
   */
  public int getBuild() {
    return build;
  }

  /**
   * <p>Getter for the field <code>major</code>.</p>
   *
   * @return a int.
   */
  public int getMajor() {
    return major;
  }

  /**
   * <p>Getter for the field <code>minor</code>.</p>
   *
   * @return a int.
   */
  public int getMinor() {
    return minor;
  }

  /**
   * <p>format</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  @NonNls
  public String format() {
    return toString();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    if ( suffix != null && suffix.length() > 0 ) {
      return major + "." + minor + '.' + build + '-' + suffix;
    } else {
      return major + "." + minor + '.' + build;
    }
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo( Version o ) {
    if ( major != o.major ) {
      return Integer.valueOf( major ).compareTo( o.major );
    }

    if ( minor != o.minor ) {
      return Integer.valueOf( minor ).compareTo( o.minor );
    }

    return Integer.valueOf( build ).compareTo( o.build );
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Version ) ) return false;

    if ( !this.getClass().equals( o.getClass() ) ) {
      return false;
    }

    Version version = ( Version ) o;

    if ( build != version.build ) return false;
    if ( major != version.major ) return false;
    if ( minor != version.minor ) return false;
    if ( suffix != null ? !suffix.equals( version.suffix ) : version.suffix != null ) return false;

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int result = major;
    result = 31 * result + minor;
    result = 31 * result + build;
    result = 31 * result + ( suffix != null ? suffix.hashCode() : 0 );
    return result;
  }

  /**
   * Liefert den Int-Werte des Versionsnummer. Major, Minor und Build gehen maximal bis 99.
   *
   * @return the int value for the version
   */
  public int toInt() {
    return major * 10000 + minor * 100 + build;
  }

  /**
   * Parses a version
   *
   * @param version the version number as string
   * @return the parsed version
   * @throws java.lang.IllegalArgumentException if any.
   */
  @NotNull
  public static Version parse( @NotNull @NonNls String version ) throws IllegalArgumentException {
    String[] parts = version.split( "\\." );
    if ( parts.length != 3 ) {
      throw new IllegalArgumentException( "Version <" + version + "> must contain exactly three parts delimited with '.'" );
    }

    int build;

    @Nullable
    String suffix;
    if ( parts[2].contains( "-" ) ) {
      int firstIndex = parts[2].indexOf( '-' );
      String buildAsString = parts[2].substring( 0, firstIndex );

      build = Integer.parseInt( buildAsString );
      suffix = parts[2].substring( firstIndex + 1, parts[2].length() );
    } else {
      build = Integer.parseInt( parts[2] );
      suffix = null;
    }

    int major = Integer.parseInt( parts[0] );
    int minor = Integer.parseInt( parts[1] );

    return new Version( major, minor, build, suffix );
  }

  /**
   * <p>verifyMatch</p>
   *
   * @param expected a {@link com.cedarsoft.Version} object.
   * @param actual a {@link com.cedarsoft.Version} object.
   * @throws com.cedarsoft.VersionMismatchException if any.
   */
  public static void verifyMatch( @NotNull Version expected, @NotNull Version actual ) throws VersionMismatchException {
    if ( !expected.equals( actual ) ) {
      throw new VersionMismatchException( expected, actual );
    }
  }

  /**
   * <p>sameOrSmallerThan</p>
   *
   * @param version a {@link com.cedarsoft.Version} object.
   * @return a boolean.
   */
  public boolean sameOrSmallerThan( @NotNull Version version ) {
    return this.compareTo( version ) <= 0;
  }

  /**
   * <p>smallerThan</p>
   *
   * @param version a {@link com.cedarsoft.Version} object.
   * @return a boolean.
   */
  public boolean smallerThan( @NotNull Version version ) {
    return this.compareTo( version ) < 0;
  }

  /**
   * <p>sameOrGreaterThan</p>
   *
   * @param version a {@link com.cedarsoft.Version} object.
   * @return a boolean.
   */
  public boolean sameOrGreaterThan( @NotNull Version version ) {
    return this.compareTo( version ) >= 0;
  }

  /**
   * <p>greaterThan</p>
   *
   * @param version a {@link com.cedarsoft.Version} object.
   * @return a boolean.
   */
  public boolean greaterThan( @NotNull Version version ) {
    return this.compareTo( version ) > 0;
  }

  /**
   * <p>valueOf</p>
   *
   * @param major a int.
   * @param minor a int.
   * @param build a int.
   * @return a {@link com.cedarsoft.Version} object.
   */
  @NotNull
  public static Version valueOf( int major, int minor, int build ) {
    return new Version( major, minor, build );
  }
}
