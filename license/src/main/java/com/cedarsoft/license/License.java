package com.cedarsoft.license;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the license of the image
 */
public class License {
  @NotNull
  @NonNls
  private final String id;
  @NotNull  @NonNls
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
}
