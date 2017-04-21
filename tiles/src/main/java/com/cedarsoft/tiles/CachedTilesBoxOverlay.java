package com.cedarsoft.tiles;

import com.cedarsoft.swing.common.NotSoVolatileImage;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.ImageCapabilities;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Adds some debugging output
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CachedTilesBoxOverlay implements Overlay {
  @Override
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    MultiThreadedObservableTilesProvider multiThreadedTilesSource = findMultiThreadedTilesSource(tilesComponent.getTilesProvider());
    CachingTilesProvider cachingTilesProvider = multiThreadedTilesSource.getCachingTilesProvider();

    ConcurrentMap<TileLocation, Tile> cacheMap = cachingTilesProvider.getCache().asMap();
    Set<TileLocation> tileLocations = cacheMap.keySet();

    g2d.translate(115, 40);
    paintBackground(g2d, 110, 15 * tileLocations.size() + 20);

    g2d.translate(0, 15);
    g2d.setColor(Color.BLACK);
    List<TileLocation> identifiers = new ArrayList<>(tileLocations);
    Collections.sort(identifiers, new Comparator<TileLocation>() {
      @Override
      public int compare(TileLocation o1, TileLocation o2) {
        int xResult = Integer.compare(o1.getX(), o2.getX());
        if (xResult != 0) {
          return xResult;
        }

        return Integer.compare(o1.getY(), o2.getY());
      }
    });

    g2d.drawString("Cached Tiles: " + identifiers.size(), 5, 0);

    for (TileLocation identifier : identifiers) {
      g2d.translate(0, 15);

      Tile tile = cacheMap.get(identifier);
      String accelerated = "n/a";
      if (tile != null) {
        if (tile instanceof NotSoVolatileImage) {
          ImageCapabilities capabilities = ((NotSoVolatileImage) tile).getVolatileImage().getCapabilities(g2d.getDeviceConfiguration());
          accelerated = capabilities.isAccelerated() ? "accel" : " slow";
          if (((NotSoVolatileImage) tile).getVolatileImage().validate(g2d.getDeviceConfiguration()) == VolatileImage.IMAGE_OK) {
            accelerated += "+";
          }
        }
        else {
          accelerated = " no volatile image";
        }
      }

      g2d.drawString(identifier.getX() + "/" + identifier.getY() + " " + accelerated, 5, 0);
    }
  }


  private static void paintBackground(@Nonnull Graphics2D g2d, int width, int height) {
    g2d.setColor(new Color(255, 255, 255, 200));
    g2d.fillRect(0, 0, width, height);
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.drawRect(0, 0, width, height);
  }

  @Nonnull
  private static MultiThreadedObservableTilesProvider findMultiThreadedTilesSource(@Nonnull TilesProvider tilesProvider) {
    TilesProvider current = tilesProvider;
    while (true) {
      if (current instanceof MultiThreadedObservableTilesProvider) {
        return (MultiThreadedObservableTilesProvider) current;
      }

      if (current instanceof DelegatingObservableTilesProvider) {
        current = ((DelegatingObservableTilesProvider) current).getDelegate();
        continue;
      }

      throw new IllegalArgumentException("Unsupported tiles source <" + tilesProvider + ">");
    }
  }
}
