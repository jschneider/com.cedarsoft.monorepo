package com.cedarsoft.tiles;

import javax.annotation.Nonnull;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Just contains one image that is returned for every tile
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class RepeatingImageTilesProvider implements TilesProvider {
  @Nonnull
  private final BufferedImage image;
  @Nonnull
  private final Tile placeHolderTile;

  public RepeatingImageTilesProvider(@Nonnull BufferedImage image) {
    this.image = image;

    BufferedImage placeHolderImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    placeHolderImage.setAccelerationPriority(1.0f);

    placeHolderTile = new ImageTile(placeHolderImage);
  }

  @Override
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int getHeight() {
    return image.getHeight();
  }

  @Nonnull
  @Override
  public Tile getTile(@Nonnull TileLocation location) {
    BufferedImage copy = copyImage(image);
    Graphics2D graphics = (Graphics2D) copy.getGraphics();
    graphics.setFont(graphics.getFont().deriveFont(15f));
    graphics.drawString(location.getX() + "/" + location.getY(), 10f, 20f);
    return new ImageTile(copy);
  }

  @Nonnull
  @Override
  public Tile getPlaceholderTile() {
    return placeHolderTile;
  }

  @Nonnull
  public static BufferedImage copyImage(@Nonnull BufferedImage source) {
    BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
    copy.setAccelerationPriority(1.0f);

    Graphics g = copy.getGraphics();
    try {
      g.drawImage(source, 0, 0, null);
    } finally {
      g.dispose();
    }
    return copy;
  }
}
