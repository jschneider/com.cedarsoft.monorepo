package com.cedarsoft.app;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class Application {
  @NotNull
  @NonNls
  private final String name;
  @NotNull
  private final Version version;

  public Application( @NotNull String name, @NotNull Version version ) {
    this.name = name;
    this.version = version;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  public Version getVersion() {
    return version;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Application ) ) return false;

    Application that = ( Application ) o;

    if ( !name.equals( that.name ) ) return false;
    if ( !version.equals( that.version ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + version.hashCode();
    return result;
  }
}
