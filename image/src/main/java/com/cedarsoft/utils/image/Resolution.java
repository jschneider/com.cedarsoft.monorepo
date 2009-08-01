package com.cedarsoft.utils.image;

import org.jetbrains.annotations.NotNull;

/**
 * For each scan images in several resoltutions are stored. This enum represents the different resolutions.
 */
public enum Resolution {
  DPI_300( 300 ),
  DPI_072( 72 );

  private final int dpi;

  Resolution( int dpi ) {
    this.dpi = dpi;
  }

  public int getDpi() {
    return dpi;
  }

  @NotNull
  public static Resolution find( int dpi ) {
    for ( Resolution resolution : values() ) {
      if ( resolution.getDpi() == dpi ) {
        return resolution;
      }
    }
    throw new IllegalArgumentException( "No resolution found for " + dpi );
  }
}
