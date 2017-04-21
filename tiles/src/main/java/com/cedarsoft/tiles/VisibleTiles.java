package com.cedarsoft.tiles;

import com.cedarsoft.unit.other.px;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Set;

/**
 * Holds currently visible tiles
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Immutable
public class VisibleTiles {
  @Nonnull
  public static final VisibleTiles NONE = new VisibleTiles(ImmutableSet.of());

  @Nonnull
  private final Set<TileLocation> identifiers;

  public VisibleTiles(@Nonnull Set<TileLocation> identifiers) {
    this.identifiers = ImmutableSet.copyOf(identifiers);
  }

  @Nonnull
  public Set<? extends TileLocation> getIdentifiers() {
    //noinspection ReturnOfCollectionOrArrayField
    return identifiers;
  }

  @Nonnull
  public static VisibleTiles create(@TileIndex int tileXTopLeft, @TileIndex int tileYTopLeft, int colCount, int rowCount, double zoomFactorX, double zoomFactorY, @px int tileWidth, @px int tileHeight) {
    ImmutableSet.Builder<TileLocation> builder = ImmutableSet.builder();

    for (int row = 0; row < rowCount; row++) {
      for (int col = 0; col < colCount; col++) {
        @TileIndex int tileX = tileXTopLeft + col;
        @TileIndex int tileY = tileYTopLeft + row;

        builder.add(new TileLocation(tileX, tileY, zoomFactorX, zoomFactorY, tileWidth, tileHeight));
      }
    }

    return new VisibleTiles(builder.build());
  }

  public int size() {
    return identifiers.size();
  }
}
