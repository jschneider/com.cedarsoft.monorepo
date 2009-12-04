package com.cedarsoft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class UnsupportedVersionException extends VersionException {
  @NotNull
  private final Version actual;

  @Nullable
  private final VersionRange supportedRange;

  public UnsupportedVersionException( @NotNull Version actual ) {
    this( actual, null );
  }

  public UnsupportedVersionException( @NotNull Version actual, @Nullable VersionRange supportedRange ) {
    super( "Unsupported version <" + actual + ">. Supported range <" + ( supportedRange == null ? "unknown" : supportedRange.toString() ) + ">" );
    this.actual = actual;
    this.supportedRange = supportedRange;
  }

  @NotNull
  public Version getActual() {
    return actual;
  }

  @Nullable
  public VersionRange getSupportedRange() {
    return supportedRange;
  }
}
