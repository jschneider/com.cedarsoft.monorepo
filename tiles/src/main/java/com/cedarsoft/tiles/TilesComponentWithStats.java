package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.concurrent.ThreadService;
import com.cedarsoft.swing.common.NotSoVolatileImage;
import com.cedarsoft.unit.si.ns;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javax.annotation.Nonnull;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Logger;

/**
 * Tiles component that keeps track of the timings
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TilesComponentWithStats extends TilesComponent {
  private static final Logger LOG = Logger.getLogger(TilesComponentWithStats.class.getName());

  @Nonnull
  private final NumberFormat format;
  /**
   * Times between starts paintComponent() calls
   */
  @Nonnull
  private final DescriptiveStatistics paintDistanceStats = new DescriptiveStatistics(200);
  @Nonnull
  private final DescriptiveStatistics paintDurationStats = new DescriptiveStatistics(200);
  @Nonnull
  private final DescriptiveStatistics paintDurationAcceleratedStats = new DescriptiveStatistics(200);
  @Nonnull
  private final DescriptiveStatistics paintDurationNonAcceleratedStats = new DescriptiveStatistics(200);
  @Nonnull
  private final DescriptiveStatistics underlayPaintDurationStats = new DescriptiveStatistics(200);
  @Nonnull
  private final DescriptiveStatistics overlayPaintDurationStats = new DescriptiveStatistics(200);
  @Nonnull
  private final DescriptiveStatistics paintTilesDurationStats = new DescriptiveStatistics(200);

  public TilesComponentWithStats(@Nonnull TilesProvider tilesProvider, @Nonnull List<Overlay> overlays, @Nonnull List<? extends Overlay> underlays, @Nonnull PanAndZoomModifier panAndZoomModifier, @Nonnull ThreadService threadService) {
    super(tilesProvider, overlays, underlays, panAndZoomModifier, threadService);

    format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits(3);
  }

  @Override
  public void scheduleRepaint(@Nonnull String reason) {
    //System.out.println("repaint called because of <" + reason + ">");
    super.scheduleRepaint(reason);
  }

  /**
   * The last time paintComponent has been called
   */
  @UiThread
  @ns
  private long lastPaintTime;

  @UiThread
  @Override
  protected void paintComponent(Graphics g) {
    @ns long beforePaint = System.nanoTime();
    super.paintComponent(g);
    @ns long afterPaint = System.nanoTime();
    //noinspection TooBroadScope remember immediatelly to avoid invalid timings
    @ns long paintDuration = afterPaint - beforePaint;
    paintDurationStats.addValue(paintDuration);

    if (lastPaintTime > 0) {
      @ns long delta = beforePaint - lastPaintTime;
      paintDistanceStats.addValue(delta);
    }

    if (isAcceleratedTile) {
      paintDurationAcceleratedStats.addValue(paintDuration);
    }
    else {
      paintDurationNonAcceleratedStats.addValue(paintDuration);
    }

    lastPaintTime = beforePaint;
  }

  private String formatNanos(long deltaPaint) {
    return format.format(deltaPaint / 1000.0 / 1000.0);
  }

  @Override
  protected void paintUnderlays(@Nonnull Graphics2D g2d) {
    @ns long before = System.nanoTime();
    super.paintUnderlays(g2d);
    long paintDuration = System.nanoTime() - before;

    underlayPaintDurationStats.addValue(paintDuration);
  }

  @Override
  protected void paintOverlays(@Nonnull Graphics2D g2d) {
    @ns long before = System.nanoTime();
    super.paintOverlays(g2d);
    long paintDuration = System.nanoTime() - before;

    overlayPaintDurationStats.addValue(paintDuration);
  }

  @Override
  protected void paintTiles(@Nonnull Graphics2D g2d) {
    @ns long before = System.nanoTime();
    super.paintTiles(g2d);
    long paintDuration = System.nanoTime() - before;

    paintTilesDurationStats.addValue(paintDuration);
  }

  /**
   * True if the last painted tile has been accelerated
   */
  @UiThread
  private boolean isAcceleratedTile;

  @Override
  protected void drawTile(@Nonnull Graphics2D g2d, @Nonnull Tile tile) {
    Image image;
    if (tile instanceof ImageTile) {
      image = ((ImageTile) tile).getImage();
    }
    else if (tile instanceof NotSoVolatileImage) {
      image = ((NotSoVolatileImage) tile).getVolatileImage();
    }
    else {
      throw new IllegalArgumentException("Unsupported tile:" + tile.getClass().getName());
    }

    isAcceleratedTile = image.getCapabilities(g2d.getDeviceConfiguration()).isAccelerated();

    if (!(tile instanceof NotSoVolatileTile)) {
      LOG.warning("ATTENTION. NON VOLATILE IMAGE PAINTED: " + tile.getClass().getName());
    }

    super.drawTile(g2d, tile);
  }

  @Nonnull
  public DescriptiveStatistics getPaintDurationStats() {
    return paintDurationStats;
  }

  @Nonnull
  public DescriptiveStatistics getPaintDurationAcceleratedStats() {
    return paintDurationAcceleratedStats;
  }

  @Nonnull
  public DescriptiveStatistics getPaintDurationNonAcceleratedStats() {
    return paintDurationNonAcceleratedStats;
  }

  @Nonnull
  public DescriptiveStatistics getPaintDistanceStats() {
    return paintDistanceStats;
  }

  @Nonnull
  public DescriptiveStatistics getUnderlayPaintDurationStats() {
    return underlayPaintDurationStats;
  }

  @Nonnull
  public DescriptiveStatistics getPaintTilesDurationStats() {
    return paintTilesDurationStats;
  }

  @Nonnull
  public DescriptiveStatistics getOverlayPaintDurationStats() {
    return overlayPaintDurationStats;
  }
}
