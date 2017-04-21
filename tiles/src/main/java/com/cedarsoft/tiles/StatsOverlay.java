package com.cedarsoft.tiles;

import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.si.ms;
import com.cedarsoft.unit.si.ns;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.text.NumberFormat;

/**
 * Shows the stats
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class StatsOverlay implements Overlay {
  /**
   * The width of the boxes
   */
  @px
  public static final int BOX_WIDTH = 200;
  @px
  public static final int BOX_HEIGHT = 112;

  /**
   * The max paint time in millis
   */
  @ms
  public static final int MAX_PAINT_DURATION_GOAL = 1000 / 60 / 4;
  @ms
  public static final int IDEAL_PAINT_DURATION_GOAL = MAX_PAINT_DURATION_GOAL / 2;
  /**
   * How many pixels represent one milli second
   */
  public static final int FACTOR_MILLIS_TO_PIXEL = 10;

  private final NumberFormat format;

  public StatsOverlay() {
    format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(3);
  }

  @Override
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    if (!(tilesComponent instanceof TilesComponentWithStats)) {
      throw new IllegalStateException("Stats overlay requires a TilesComponentWithStats");
    }

    TilesComponentWithStats tilesComponentWithStats = (TilesComponentWithStats) tilesComponent;

    //top left corner
    g2d.translate(5, 5);
    paintDurationsGraph(g2d, BOX_WIDTH, tilesComponentWithStats.getPaintDurationStats().getValues());
    paintSection("Paint Durations", g2d, tilesComponentWithStats.getPaintDurationStats(), BOX_WIDTH);

    g2d.translate(0, 12);
    paintDurationsGraph(g2d, BOX_WIDTH, tilesComponentWithStats.getPaintDurationAcceleratedStats().getValues());
    paintSection("Accelerated", g2d, tilesComponentWithStats.getPaintDurationAcceleratedStats(), BOX_WIDTH);

    g2d.translate(0, 12);
    paintDurationsGraph(g2d, BOX_WIDTH, tilesComponentWithStats.getPaintDurationNonAcceleratedStats().getValues());
    paintSection("Unaccelerated", g2d, tilesComponentWithStats.getPaintDurationNonAcceleratedStats(), BOX_WIDTH);

    g2d.translate(0, 12);
    paintDurationsGraph(g2d, BOX_WIDTH, tilesComponentWithStats.getUnderlayPaintDurationStats().getValues());
    paintSection("Underlays", g2d, tilesComponentWithStats.getUnderlayPaintDurationStats(), BOX_WIDTH);

    g2d.translate(0, 12);
    paintDurationsGraph(g2d, BOX_WIDTH, tilesComponentWithStats.getPaintTilesDurationStats().getValues());
    paintSection("Tiles", g2d, tilesComponentWithStats.getPaintTilesDurationStats(), BOX_WIDTH);

    g2d.translate(0, 12);
    paintDurationsGraph(g2d, BOX_WIDTH, tilesComponentWithStats.getOverlayPaintDurationStats().getValues());
    paintSection("Overlays", g2d, tilesComponentWithStats.getOverlayPaintDurationStats(), BOX_WIDTH);

    g2d.translate(0, 12);
    fillBoxBackground(g2d, BOX_WIDTH, BOX_HEIGHT);
    paintSection("Paint Distance", g2d, tilesComponentWithStats.getPaintDistanceStats(), BOX_WIDTH);

    @Nullable MultiThreadedObservableTilesProvider multiThreadedTilesSource = findMultiThreadedTilesSource(tilesComponent.getTilesProvider());
    if (multiThreadedTilesSource != null) {
      g2d.translate(0, 12);
      LoadingCache<TileLocation, Tile> cache = multiThreadedTilesSource.getCachingTilesProvider().getCache();
      paintCacheStats("Tiles Cache: ", g2d, cache.stats(), cache.size(), BOX_WIDTH);
    }
  }

  private static void paintDurationsGraph(@Nonnull Graphics2D g2d, int width, @ns double[] durations) {
    fillBoxBackground(g2d, width, BOX_HEIGHT);

    Graphics2D copy = (Graphics2D) g2d.create();
    try {
      copy.setClip(0, 0, width, BOX_HEIGHT);

      //each pixel represents one value
      for (int i = 0; i < durations.length; i++) {
        @ms double durationInMillis = durations[i] / 1000.0 / 1000.0;
        copy.setColor(getDurationBarColor(durationInMillis));

        @px double barHeight = Math.max(2, durationInMillis * FACTOR_MILLIS_TO_PIXEL);
        copy.draw(new Line2D.Double(i, BOX_HEIGHT, i, BOX_HEIGHT - barHeight));
      }

      //Draw the lines
      copy.setColor(Color.BLACK);
      @px int yValueMaxDurationGoal = BOX_HEIGHT - MAX_PAINT_DURATION_GOAL * FACTOR_MILLIS_TO_PIXEL;
      copy.draw(new Line2D.Double(0, yValueMaxDurationGoal, width, yValueMaxDurationGoal));

      copy.setColor(new Color(0, 0, 0, 100));
      @px int yValueIdealGoal = BOX_HEIGHT - IDEAL_PAINT_DURATION_GOAL * FACTOR_MILLIS_TO_PIXEL;
      copy.draw(new Line2D.Double(0, yValueIdealGoal, width, yValueIdealGoal));
    } finally {
      copy.dispose();
    }
  }

  /**
   * Returns the color that is used for the bars
   */
  @Nonnull
  private static Color getDurationBarColor(@ms double durationInMillis) {
    if (durationInMillis > MAX_PAINT_DURATION_GOAL) {
      return Color.RED;
    }

    if (durationInMillis <= IDEAL_PAINT_DURATION_GOAL) {
      return Color.GREEN;
    }

    return Color.ORANGE;
  }

  @Nullable
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

      return null;
    }
  }

  private void paintCacheStats(@Nonnull String category, @Nonnull Graphics2D g2d, @Nonnull CacheStats stats, long size, int width) {
    g2d.setColor(new Color(255, 255, 255, 200));
    int height = 160;
    g2d.fillRect(0, 0, width, height);
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.drawRect(0, 0, width, height);

    g2d.setColor(Color.BLACK);
    g2d.translate(0, 10);
    g2d.drawString(category + ":", 3, 3);
    g2d.translate(0, 16);
    g2d.drawString("Cache Size: " + format.format(size), 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Avrg. Load Penalty: " + formatMillis(stats.averageLoadPenalty() / 1000.0 / 1000.0) + " ms", 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Eviction Count: " + format.format(stats.evictionCount()), 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Hit Count: " + format.format(stats.hitCount()), 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Miss Count: " + format.format(stats.missCount()), 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Hit Rate: " + formatMillis(stats.hitRate()), 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Miss Rate: " + formatMillis(stats.missRate()), 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Load Time: " + formatMillis(stats.totalLoadTime() / 1000.0 / 1000.0) + " ms", 13, 3);
  }

  private void paintSection(@Nonnull String category, @Nonnull Graphics2D g2d, @Nonnull DescriptiveStatistics durationStats, int width) {
    g2d.setColor(Color.BLACK);
    g2d.translate(0, 10);
    g2d.drawString(category + ":", 3, 3);
    g2d.translate(0, 16);

    String lastValue;
    long count = durationStats.getN();
    if (count == 0) {
      lastValue = "";
    }
    else {
      lastValue = formatMillis(durationStats.getElement((int) (count - 1)) / 1000.0 / 1000.0);
    }

    g2d.drawString("Last value: " + lastValue + " ms", 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Mean: " + formatMillis(durationStats.getMean() / 1000.0 / 1000.0) + " ms", 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Max: " + formatMillis(durationStats.getMax() / 1000.0 / 1000.0) + " ms", 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Min: " + formatMillis(durationStats.getMin() / 1000.0 / 1000.0) + " ms", 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Count: " + this.format.format(durationStats.getN()), 13, 3);
    g2d.translate(0, 16);
    g2d.drawString("Sum: " + formatMillis(durationStats.getSum() / 1000.0 / 1000.0) + " ms", 13, 3);
  }

  @Nonnull
  private String formatMillis(double number) {
    if (Double.isNaN(number)) {
      return "NaN";
    }
    return this.format.format(number);
  }

  private static void fillBoxBackground(@Nonnull Graphics2D g2d, int width, int height) {
    g2d.setColor(new Color(255, 255, 255, 200));
    g2d.fillRect(0, 0, width, height);
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.drawRect(0, 0, width, height);
  }
}
