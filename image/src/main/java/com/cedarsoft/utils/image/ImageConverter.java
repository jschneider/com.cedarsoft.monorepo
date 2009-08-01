package com.cedarsoft.utils.image;

import org.jetbrains.annotations.NotNull;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 */
public class ImageConverter {
  /**
   * Calculates the new dimension of the image
   *
   * @param original           the original image
   * @param originalResolution the original resolution
   * @param targetResolution   the target resolution
   * @return the dimension
   */
  public Dimension calculateNewDimensions( @NotNull BufferedImage original, @NotNull Resolution originalResolution, @NotNull Resolution targetResolution ) {
    int newWidth = original.getWidth() * targetResolution.getDpi() / originalResolution.getDpi();
    int newHeight = original.getHeight() * targetResolution.getDpi() / originalResolution.getDpi();
    return new Dimension( newWidth, newHeight );
  }

  /**
   * Resizes the image to the given size
   */
  @NotNull
  public BufferedImage resize( @NotNull BufferedImage original, @NotNull Dimension targetDimension ) {
    BufferedImage resized = new BufferedImage( targetDimension.width, targetDimension.height, BufferedImage.TYPE_INT_RGB );
    Graphics2D graphics2D = resized.createGraphics();
    graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
    graphics2D.drawImage( original, 0, 0, targetDimension.width, targetDimension.height, null );
    return resized;
  }

  @NotNull
  public BufferedImage resize( @NotNull BufferedImage original, @NotNull Resolution originalResolution, @NotNull Resolution targetResolution ) {
    return resize( original, calculateNewDimensions( original, originalResolution, targetResolution ) );
  }
}
