package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A base name of a file
 */
public class BaseName implements Comparable<BaseName>, Serializable {
  @NotNull
  @NonNls
  private final String name;

  /**
   * Creates a new base name
   *
   * @param name the name
   */
  public BaseName( @NonNls @NotNull String name ) {
    this.name = name;
  }

  @NonNls
  @NotNull
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof BaseName ) ) return false;

    BaseName baseName = ( BaseName ) o;

    if ( !name.equals( baseName.name ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public int compareTo( BaseName o ) {
    return name.compareTo( o.name );
  }
}
