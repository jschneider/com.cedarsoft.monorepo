package com.cedarsoft;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class UnsupportedVersionRangeException extends VersionException {
  @NotNull
  private final VersionRange actual;

  @Nullable
  private final VersionRange supportedRange;

  public UnsupportedVersionRangeException( @NotNull VersionRange actual ) {
    this( actual, null );
  }

  public UnsupportedVersionRangeException( @NotNull VersionRange actual, @Nullable VersionRange supportedRange ) {
    super( "Unsupported version range <" + actual + ">. Supported range <" + ( supportedRange == null ? "unknown" : supportedRange.toString() ) + ">" );
    this.actual = actual;
    this.supportedRange = supportedRange;
  }

  @NotNull
  public VersionRange getActual() {
    return actual;
  }

  @Nullable
  public VersionRange getSupportedRange() {
    return supportedRange;
  }
}