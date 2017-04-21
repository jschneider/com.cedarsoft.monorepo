package com.cedarsoft.tiles;

import javax.annotation.Nonnull;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Default tile implementation that uses an image.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ImageTile implements Tile {
  @Nonnull
  private final Image image;

  public ImageTile(@Nonnull Image image) {
    this.image = image;
  }

  @Override
  public void draw(@Nonnull Graphics2D g2d, int x, int y) {
    g2d.drawImage(image, x, y, null);
  }

  @Nonnull
  public Image getImage() {
    return image;
  }

  @Override
  public void dispose() {
    //do nothing
  }
}
