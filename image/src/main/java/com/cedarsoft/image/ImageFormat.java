package com.cedarsoft.image;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * The format of the scanned image
 */
public enum ImageFormat {
  JPEG( "jpg" );

  @NotNull
  @NonNls
  private final String suffix;

  ImageFormat( String suffix ) {
    this.suffix = suffix;
  }

  @NotNull
  @NonNls
  public String getSuffix() {
    return suffix;
  }

  @NotNull
  public static ImageFormat find( String suffix ) {
    for ( ImageFormat format : values() ) {
      if ( format.getSuffix().equals( suffix ) ) {
        return format;
      }
    }
    throw new IllegalArgumentException( "No ImageFormat found for suffix " + suffix );
  }
}
