package com.cedarsoft.license;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the license of the image
 */
public class License {
  @NotNull
  public static final License UNKNOWN = new License( "UNKNOWN", "Unknown" );
  @NotNull
  public static final License ALL_RIGHTS_RESERVED = new License( "ALL_RIGHTS_RESERVED", "All rights reserved" );
  @NotNull
  public static final License PUBLIC_DOMAIN = new License( "PUBLIC_DOMAIN", "Public Domain" );

  @NotNull
  @NonNls
  private final String id;
  @NotNull
  @NonNls
  private final String name;

  public License( @NotNull @NonNls String id, @NotNull @NonNls String name ) {
    this.id = id;
    this.name = name;
  }

  @NotNull
  public String getName() {
    return name;
  }

  @NotNull
  @NonNls
  public String getId() {
    return id;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof License ) ) return false;

    License license = ( License ) o;

    if ( !id.equals( license.id ) ) return false;
    if ( !name.equals( license.name ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }
}
