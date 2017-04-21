package com.cedarsoft.tiles;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class VisibleTilesOverlay implements Overlay {
  @Override
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    VisibleTiles visibleTiles = tilesComponent.getVisibleTiles();

    g2d.translate(0, 40);

    paintBackground(g2d, 110, 15 * visibleTiles.size() + 20);

    g2d.translate(0, 15);
    g2d.setColor(Color.BLACK);
    List<TileLocation> identifiers = new ArrayList<>(visibleTiles.getIdentifiers());

    Collections.sort(identifiers, (o1, o2) -> {
      int xResult = Integer.compare(o1.getX(), o2.getX());
      if (xResult != 0) {
        return xResult;
      }

      return Integer.compare(o1.getY(), o2.getY());
    });

    g2d.drawString("Visible Tiles: " + identifiers.size(), 5, 0);

    for (TileLocation identifier : identifiers) {
      g2d.translate(0, 15);
      g2d.drawString(identifier.getX() + "/" + identifier.getY(), 5, 0);
    }
  }


  private static void paintBackground(@Nonnull Graphics2D g2d, int width, int height) {
    g2d.setColor(new Color(255, 255, 255, 200));
    g2d.fillRect(0, 0, width, height);
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.drawRect(0, 0, width, height);
  }
}
