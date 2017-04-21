package com.cedarsoft.tiles;

import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Shows information about the tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TilesInformationOverlay implements Overlay {

  public static final Color COLOR = new Color(0, 221, 117, 61);

  @Override
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    @px int width = tilesComponent.getTilesProvider().getWidth();
    @px int height = tilesComponent.getTilesProvider().getHeight();

    VisibleTiles visibleTiles = tilesComponent.getVisibleTiles();
    for (TileLocation tileLocation : visibleTiles.getIdentifiers()) {

      @px @VisibleArea double minXComponent = tilesComponent.getConverter().view2VisibleAreaX(tileLocation.getMinX());
      @px @VisibleArea double minYComponent = tilesComponent.getConverter().view2VisibleAreaY(tileLocation.getMinY());
      @px @VisibleArea double maxXComponent = tilesComponent.getConverter().view2VisibleAreaX(tileLocation.getMaxX());
      @px @VisibleArea double maxYComponent = tilesComponent.getConverter().view2VisibleAreaY(tileLocation.getMaxY());


      g2d.setColor(COLOR);
      g2d.draw(new Rectangle2D.Double(minXComponent, minYComponent, width - 1, height - 1));

      //Top left
      String description = tileLocation.getX() + "/" + tileLocation.getY();
      @px int descriptionWidth = g2d.getFontMetrics().stringWidth(description);

      g2d.drawString(description, (float) minXComponent + 5, (float) minYComponent + 12);

      //Bottom left
      g2d.drawString(description, (float) minXComponent + 5, (float) maxYComponent - 5);

      //Top right
      g2d.drawString(description, (float) maxXComponent - descriptionWidth - 5, (float) minYComponent + 12);

      //Bottom right
      g2d.drawString(description, (float) maxXComponent - descriptionWidth - 5, (float) maxYComponent - 5);
    }
  }
}
