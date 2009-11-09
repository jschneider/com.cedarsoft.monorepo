package com.cedarsoft;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of a file
 */
public class Extension {
  @NotNull
  @NonNls
  private final String delimiter;
  @NotNull
  @NonNls
  private final String extension;

  public Extension( @NonNls @NotNull String delimiter, @NotNull @NonNls String extension ) {
    this.delimiter = delimiter;
    this.extension = extension;
  }

  @NotNull
  @NonNls
  public String getDelimiter() {
    return delimiter;
  }

  @NotNull
  @NonNls
  public String getExtension() {
    return extension;
  }

  @NotNull
  @NonNls
  public String getCombined() {
    return delimiter + extension;
  }

  @Override
  public String toString() {
    return getCombined();
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Extension ) ) return false;

    Extension extension1 = ( Extension ) o;

    if ( !delimiter.equals( extension1.delimiter ) ) return false;
    if ( !extension.equals( extension1.extension ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = delimiter.hashCode();
    result = 31 * result + extension.hashCode();
    return result;
  }
}
