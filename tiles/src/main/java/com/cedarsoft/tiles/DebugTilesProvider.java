package com.cedarsoft.tiles;

import com.cedarsoft.swing.common.ColorTools;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;

/**
 * Tiles provider that can be used to show some debugging tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DebugTilesProvider implements TilesProvider {
  public static final int SIZE = 400;

  @Nonnull
  private final Tile placeholderTile;

  public DebugTilesProvider() {
    BufferedImage placeHolderImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics = (Graphics2D) placeHolderImage.getGraphics();

    graphics.setColor(ColorTools.interpolate(0.5, 0.4, 0.6));
    graphics.fillRect(0, 0, SIZE, SIZE);

    graphics.setColor(Color.BLUE);
    graphics.drawRect(0, 0, SIZE - 1, SIZE - 1);
    graphics.setColor(Color.WHITE);

    graphics.dispose();

    placeholderTile = new ImageTile(placeHolderImage);
  }

  @Override
  public int getWidth() {
    return SIZE;
  }

  @Override
  public int getHeight() {
    return SIZE;
  }

  @Nonnull
  @Override
  public Tile getPlaceholderTile() {
    return placeholderTile;
  }

  @Nonnull
  @Override
  public Tile getTile(@Nonnull TileLocation location) {
    @TileIndex int tileX = location.getX();
    @TileIndex int tileY = location.getY();
    double zoomFactorX = location.getZoomFactorX();
    double zoomFactorY = location.getZoomFactorY();

    BufferedImage bufferedImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

    double percent = Math.sin(tileX * 0.0200 * tileY);
    graphics.setColor(ColorTools.interpolate(percent, 0.4, 0.6));
    graphics.fillRect(0, 0, SIZE, SIZE);

    graphics.setColor(Color.BLUE);
    graphics.drawRect(0, 0, SIZE - 1, SIZE - 1);
    graphics.setColor(Color.WHITE);

    //Now paint the data simulation
    int depthCount = 1;

    //TODO where to use zoom factor y?
    double delta = 30 * zoomFactorX;
    Color dataColor = Color.LIGHT_GRAY;
    while (delta < 3) {
      delta *= 10;
      dataColor = darker(dataColor);
      depthCount++;
    }
    while (delta > 50) {
      delta /= 10;
      dataColor = lighter(dataColor);
      depthCount--;
    }
    graphics.setColor(dataColor);

    for (double i = 0; i < SIZE; i += delta) {
      graphics.draw(new Line2D.Double(i, 0, i, SIZE));
      graphics.draw(new Line2D.Double(0, i, SIZE, i));
    }

    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(3);
    graphics.drawString(format.format(zoomFactorX), 12, 12);

    graphics.setColor(Color.WHITE);
    graphics.setFont(graphics.getFont().deriveFont(30f));
    graphics.drawString(String.valueOf(depthCount), 50, 50);


    //Now draw the data
    TileTranslation translation = new TileTranslation(tileX, tileY, zoomFactorX, zoomFactorY, getWidth(), getHeight());


    //Output the min/max values
    {
      graphics.setColor(Color.GRAY);
      graphics.setFont(graphics.getFont().deriveFont(12f));

      graphics.drawString(format.format(translation.getMinXModel()), 5, (float) (SIZE / 2.0 - 5));

      {
        String maxXString = format.format(translation.getMaxXModel());
        double stringWidth = graphics.getFontMetrics().getStringBounds(maxXString, graphics).getWidth();
        graphics.drawString(maxXString, (float) (getWidth() - stringWidth) - 5, (float) (SIZE / 2.0 - 5));
      }

      {
        String minYString = format.format(translation.getMinYModel());
        double stringWidth = graphics.getFontMetrics().getStringBounds(minYString, graphics).getWidth();
        graphics.drawString(minYString, (float) (getWidth() / 2.0 - stringWidth / 2.0), 15);
      }
      {
        String maxYString = format.format(translation.getMaxXModel());
        double stringWidth = graphics.getFontMetrics().getStringBounds(maxYString, graphics).getWidth();
        graphics.drawString(maxYString, (float) (getWidth() / 2.0 - stringWidth / 2.0), SIZE - 5);
      }
    }

    //draw line x=y
    graphics.setColor(Color.ORANGE);
    graphics.draw(translation.transformToTile(new Line2D.Double(translation.getMinXModel(), translation.getMinXModel(), translation.getMaxXModel(), translation.getMaxXModel())));
    //draw line x=-y
    graphics.draw(translation.transformToTile(new Line2D.Double(translation.getMinXModel(), -translation.getMinXModel(), translation.getMaxXModel(), -translation.getMaxXModel())));


    //Add a pointer to origin
    graphics.setColor(Color.GREEN);
    graphics.draw(translation.transformToTile(new Line2D.Double(translation.getCenterModel(), new Point2D.Double(0, 0))));

    //Add a sin
    {
      graphics.setColor(Color.BLUE);

      boolean isWithinYRange = false;

      long tileWidth = translation.getTileWidth();
      @Model double[] yValues = new double[(int) tileWidth];
      for (@px int x = 0; x < tileWidth; x++) {
        @Model double modelX = translation.tile2modelX(x);
        @Model double modelY = Math.sin(modelX / 105) * 405;
        yValues[x] = modelY;

        isWithinYRange = isWithinYRange || translation.isInModelYRange(modelY);
      }

      //Only paint if
      if (isWithinYRange) {
        @px Path2D.Double path = new Path2D.Double();
        path.moveTo(0, 0);
        for (@px int i = 0; i < yValues.length; i++) {
          @Model double yValue = yValues[i];
          path.lineTo(i, translation.model2tileY(yValue));
        }

        graphics.draw(path);
      }
    }

    return new ImageTile(bufferedImage);
  }

  @Nonnull
  public static Color darker(@Nonnull Color base) {
    double factor = 0.9;
    return new Color(Math.max((int) (base.getRed() * factor), 0),
                     Math.max((int) (base.getGreen() * factor), 0),
                     Math.max((int) (base.getBlue() * factor), 0),
                     base.getAlpha());
  }

  @Nonnull
  public static Color lighter(@Nonnull Color base) {
    double factor = 1.1;
    return new Color(Math.min((int) (base.getRed() * factor), 255),
                     Math.min((int) (base.getGreen() * factor), 255),
                     Math.min((int) (base.getBlue() * factor), 255),
                     base.getAlpha());
  }
}
