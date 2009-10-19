package com.cedarsoft.app;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A version
 */
public class Version implements Comparable<Version> {
  protected static final int MAX = 99;

  protected final int major;
  protected final int minor;
  protected final int build;
  @Nullable
  @NonNls
  protected final String suffix;


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

  public int getBuild() {
    return build;
  }

  public int getMajor() {
    return major;
  }

  public int getMinor() {
    return minor;
  }

  @NotNull
  @NonNls
  public String format() {
    return toString();
  }

  @Override
  public String toString() {
    if ( suffix != null && suffix.length() > 0 ) {
      return major + "." + minor + '.' + build + '-' + suffix;
    } else {
      return major + "." + minor + '.' + build;
    }
  }

  public int compareTo( Version o ) {
    if ( major != o.major ) {
      return Integer.valueOf( major ).compareTo( o.major );
    }

    if ( minor != o.minor ) {
      return Integer.valueOf( minor ).compareTo( o.minor );
    }

    return Integer.valueOf( build ).compareTo( o.build );
  }

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
   *
   * @throws IllegalArgumentException
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
}
