package com.cedarsoft.tiles;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.concurrent.SwingAsync;
import com.cedarsoft.concurrent.ThreadService;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.si.ns;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Component that shows tiles
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class TilesComponent extends JComponent {
  private static final Logger LOG = Logger.getLogger(TilesComponent.class.getName());

  public static final double SQRT_2 = 1.4142135623730951;

  public static final int MAX_ZOOM_LEVEL = 15;
  public static final int MAX_ZOOM_LEVEL_1S_RESOLUTION = 6;

  public final int maxZoomLevel;

  public static final double MAX_ZOOM_FACTOR = zoomLevel2Factor(MAX_ZOOM_LEVEL); //256

  public final double maxZoomFactor;

  public static final int MIN_ZOOM_LEVEL = -20;

  public static final double MIN_ZOOM_FACTOR = zoomLevel2Factor(MIN_ZOOM_LEVEL);


  private static final int FRAMES_PER_SECOND = 60;

  public static final int DEFAULT_ZOOM_LEVEL = 3;

  public static final double DEFAULT_ZOOM_FACTOR = zoomLevel2Factor(DEFAULT_ZOOM_LEVEL);


  public static double zoomLevel2Factor(int zoomLevel) {
    return Math.pow(SQRT_2, zoomLevel);
  }


  private static final int ZOOM_LEVEL_DISPLAY_OFFSET = 19;


  @Nonnull
  private final SwingAsync async = new SwingAsync();

  @Nonnull
  private final TilesProvider tilesProvider;

  @Nonnull
  @UiThread
  private final List<Overlay> overlays = new ArrayList<>();
  @Nonnull
  @UiThread
  private final List<Overlay> underlays = new ArrayList<>();

  @View
  @px
  private final Point2D.Double viewMovement = new Point2D.Double();

  private double zoomFactorX = 1;
  private double zoomFactorY = 1;

  @Nonnull
  private final PanAndZoomModifier panAndZoomModifier;

  @Nonnull
  private VisibleTiles visibleTiles = VisibleTiles.NONE;

  @UiThread
  @NonUiThread
  private volatile boolean dirty;

  @UiThread
  private final List<TimedUpdater> timedUpdaters = new ArrayList<>();

  @Nonnull
  private final FramePerSecondsCalculator framePerSecondsCalculator = new FramePerSecondsCalculator();

  @Nonnull
  private final Timer timer;

  @Nonnull
  private final Converter converter;

  public TilesComponent(@Nonnull TilesProvider tilesProvider, @Nonnull List<Overlay> overlays, @Nonnull List<? extends Overlay> underlays, @Nonnull PanAndZoomModifier panAndZoomModifier, @Nonnull ThreadService threadService) {
    this.tilesProvider = tilesProvider;
    this.underlays.addAll(underlays);
    this.overlays.addAll(overlays);
    this.panAndZoomModifier = panAndZoomModifier;

    maxZoomFactor = MAX_ZOOM_FACTOR;
    maxZoomLevel = MAX_ZOOM_LEVEL;

    converter = new Converter(this);

    setDoubleBuffered(false);

    //modify the panning initially if necessary
    panAndZoomModifier.modifyPanning(this, viewMovement, zoomFactorX, zoomFactorY);

    if (tilesProvider instanceof ObservableTilesProvider) {
      ((ObservableTilesProvider) tilesProvider).addListener(new ObservableTilesProvider.Listener() {
        @Override
        @NonUiThread
        public void tileUpdated(@Nonnull ObservableTilesProvider source, @Model double modelXMin, @Model double modelXMax) {
          if (LOG.isLoggable(Level.FINER)) {
            LOG.finer("tile updated <" + modelXMin + " - " + modelXMax + ">");
          }
          async.last(() -> dirty = true);
        }
      });
    }

    updateVisibleTiles();

    timer = threadService.newSwingTimer(1000 / FRAMES_PER_SECOND);
    timer.addActionListener(e -> {
      //Do not repaint if not showing
      if (!isShowing()) {
        return;
      }

      for (TimedUpdater timedUpdater : timedUpdaters) {
        timedUpdater.update(TilesComponent.this);
      }

      if (dirty) {
        dirty = false;
        framePerSecondsCalculator.newFrame();
        TilesComponent.super.repaint();
      }
    });

    addComponentListener(new ComponentListener() {
      @Override
      public void componentResized(ComponentEvent e) {
        updateVisibleTiles();
      }

      @Override
      public void componentMoved(ComponentEvent e) {
      }

      @Override
      public void componentShown(ComponentEvent e) {
        updateVisibleTiles();
        timer.start();
      }

      @Override
      public void componentHidden(ComponentEvent e) {
        timer.stop();
      }
    });

    //Start/stop timer if component is shown/hidden
    addHierarchyListener(e -> {
      if (e.getComponent() != TilesComponent.this) {
        return;
      }

      if (isDisplayable()) {
        if (!timer.isRunning()) {
          timer.start();
        }
      }
      else {
        timer.stop();
      }
    });

    //Starts the timer
    timer.start();

    //Handle focus
    setRequestFocusEnabled(true);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        requestFocus();
      }
    });
  }

  public void centerViewTo(@View @px double centerX, @View @px double centerY) {
    moveViewTo(centerX + getWidth() / 2.0, centerY + getHeight() / 2.0);
  }

  @UiThread
  public void moveViewTo(@View @px double x, @View @px double y) {
    //zoom factor is not relevant
    viewMovement.x = x;
    viewMovement.y = y;

    panAndZoomModifier.modifyPanning(this, viewMovement, zoomFactorX, zoomFactorY);

    updateVisibleTiles();
    scheduleRepaint("view moved to");
  }

  @UiThread
  public void moveView(@View @px double deltaX, @View @px double deltaY) {
    //zoom factor is not relevant
    viewMovement.x += deltaX;
    viewMovement.y += deltaY;

    panAndZoomModifier.modifyPanning(this, viewMovement, zoomFactorX, zoomFactorY);

    updateVisibleTiles();
    scheduleRepaint("view moved");
  }

  @UiThread
  public void zoomView(int changeAmountX, int changeAmountY) {
    zoomView(changeAmountX, changeAmountY, null);
  }

  @UiThread
  public void zoomView(int changeAmountX, int changeAmountY, @VisibleArea @px @Nullable Point2D zoomCenter) {
    double newZoomFactorX = zoomFactorX * zoomLevel2Factor(-changeAmountX);
    double newZoomFactorY = zoomFactorY * zoomLevel2Factor(-changeAmountY);

    setZoomFactor(newZoomFactorX, newZoomFactorY, getComponentCenter());
  }

  @UiThread
  public void resetZoom() {
    setZoomFactor(DEFAULT_ZOOM_FACTOR, DEFAULT_ZOOM_FACTOR);
  }

  @UiThread
  public void zoomIn() {
    Point2D.Double zoomCenter = getComponentCenter();
    zoomView(-1, -1, zoomCenter);
  }

  @UiThread
  public void zoomOut() {
    Point2D.Double zoomCenter = getComponentCenter();
    zoomView(1, 1, zoomCenter);
  }

  @px
  @VisibleArea
  @Nonnull
  private Point2D.Double getComponentCenter() {
    return new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0);
  }

  @UiThread
  public void setZoomFactor(double zoomFactorX, double zoomFactorY) {
    setZoomFactor(zoomFactorX, zoomFactorY, getComponentCenter());
  }

  @UiThread
  public void setZoomFactor(double newZoomFactorX, double newZoomFactorY, @VisibleArea @px @Nonnull Point2D zoomCenter) {
    //remember the old settings
    double oldZoomFactorX = this.zoomFactorX;
    double oldZoomFactorY = this.zoomFactorY;

    //Remember the model location under the zoom center before applying the zoom change
    @Model Point2D.Double modelZoomCenter = getConverter().visibleArea2Model(zoomCenter.getX(), zoomCenter.getY());

    zoomFactorX = newZoomFactorX;
    zoomFactorY = newZoomFactorY;

    //ensure the limits
    double[] zoomFactors = panAndZoomModifier.modifyZoomFactors(new double[]{zoomFactorX, zoomFactorY});

    zoomFactorX = Math.max(TilesComponent.MIN_ZOOM_FACTOR, Math.min(zoomFactors[0], maxZoomFactor));
    zoomFactorY = Math.max(TilesComponent.MIN_ZOOM_FACTOR, Math.min(zoomFactors[1], maxZoomFactor));

    //which model value is now under the zoom center? Necessary to calculate the correction
    @Model Point2D.Double newModelZoomCenter = getConverter().visibleArea2Model(zoomCenter.getX(), zoomCenter.getY());

    //Move the view so that the old model point is again placed under the zoom center
    @Model double deltaX = newModelZoomCenter.x - modelZoomCenter.x;
    @Model double deltaY = newModelZoomCenter.y - modelZoomCenter.y;
    viewMovement.x += deltaX * zoomFactorX;
    viewMovement.y += deltaY * zoomFactorY;

    updateVisibleTiles();
    scheduleRepaint("view zoomed");

    notifyZoomFactorUpdated(oldZoomFactorX, oldZoomFactorY, this.zoomFactorX, this.zoomFactorY);
  }

  public int getZoomLevelX() {
    double factor = Math.log(zoomFactorX) / Math.log(TilesComponent.SQRT_2);
    return (int) Math.round(factor) - MIN_ZOOM_LEVEL - ZOOM_LEVEL_DISPLAY_OFFSET;
  }

  public int getZoomLevelY() {
    double factor = Math.log(zoomFactorY) / Math.log(TilesComponent.SQRT_2);
    return (int) Math.round(factor) - MIN_ZOOM_LEVEL - ZOOM_LEVEL_DISPLAY_OFFSET;
  }

  private void updateVisibleTiles() {
    @px int tilesWidth = getTilesProvider().getWidth();
    @px int tilesHeight = getTilesProvider().getHeight();

    //The value that is shown in the top left of the component
    @View @px Point2D.Double shownInOrigin = getViewInOrigin();

    //Tile index in top left corner
    @TileIndex int tileXTopLeft = (int) Math.floor(shownInOrigin.x / tilesWidth);
    @TileIndex int tileYTopLeft = (int) Math.floor(shownInOrigin.y / tilesHeight);

    //Calculate the amount of rows/cols
    int colCount = (int) (Math.ceil((double) getWidth() / tilesWidth) + 1);
    int rowCount = (int) (Math.ceil((double) getHeight() / tilesHeight) + 1);

    visibleTiles = VisibleTiles.create(tileXTopLeft, tileYTopLeft, colCount, rowCount, zoomFactorX, zoomFactorY, tilesWidth, tilesHeight);

    if (tilesProvider instanceof VisibleTilesAwareTilesProvider) {
      ((VisibleTilesAwareTilesProvider) tilesProvider).visibleTilesChanged(this, visibleTiles);
    }
  }

  @View
  @px
  @UiThread
  @Nonnull
  public Point2D.Double getViewInOrigin() {
    return new Point2D.Double(-viewMovement.x, -viewMovement.y);
  }

  public void addOverlay(@Nonnull Overlay overlay) {
    this.overlays.add(overlay);
  }

  public void addUnderlay(@Nonnull Overlay underlay) {
    this.underlays.add(underlay);
  }

  @UiThread
  @NonUiThread
  public void scheduleRepaint(@Nonnull String reason) {
    dirty = true;
  }

  @Override
  public void repaint() {
    dirty = true;
  }

  @UiThread
  @NonUiThread
  public void markAsDirty() {
    dirty = true;
  }

  @UiThread
  public void addUpdater(@Nonnull TimedUpdater timedUpdater) {
    this.timedUpdaters.add(timedUpdater);
  }

  @UiThread
  public void removeUpdater(@Nonnull TimedUpdater timedUpdater) {
    this.timedUpdaters.remove(timedUpdater);
  }

  @UiThread
  @Override
  protected void paintComponent(Graphics g) {
    //do NOT call super.paintComponent()
    Graphics2D g2d = (Graphics2D) g;

    //background
    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, getWidth(), getHeight());

    paintUnderlays(g2d);
    paintTiles(g2d);
    paintOverlays(g2d);
  }

  protected void paintOverlays(@Nonnull Graphics2D g2d) {
    for (Overlay overlay : overlays) {
      Graphics2D copy = (Graphics2D) g2d.create();
      try {
        overlay.paint(copy, this);
      } finally {
        copy.dispose();
      }
    }
  }

  protected void paintTiles(@Nonnull Graphics2D g2d) {
    for (TileLocation identifier : visibleTiles.getIdentifiers()) {
      drawTile(g2d, identifier);
    }
  }

  protected void paintUnderlays(@Nonnull Graphics2D g2d) {
    for (Overlay underlay : underlays) {
      Graphics2D copy = (Graphics2D) g2d.create();
      try {
        underlay.paint(copy, this);
      } finally {
        copy.dispose();
      }
    }
  }

  protected void drawTile(@Nonnull Graphics2D g2d, @Nonnull TileLocation identifier) {
    drawTile(g2d, identifier.getX(), identifier.getY());
  }

  @UiThread
  protected void drawTile(@Nonnull Graphics2D g2, @TileIndex int tileX, @TileIndex int tileY) {
    @px int tilesWidth = getTilesProvider().getWidth();
    @px int tilesHeight = getTilesProvider().getHeight();

    @px @VisibleArea double tileOriginX = (long) tilesWidth * tileX + viewMovement.x;
    @px @VisibleArea double tileOriginY = (long) tilesHeight * tileY + viewMovement.y;

    @px @VisibleArea double tileMaxX = tileOriginX + tilesWidth + viewMovement.x;
    @px @VisibleArea double tileMaxY = tileOriginY + tilesHeight + viewMovement.y;


    //Check if the tile is visible, else return
    //TODO add otpimization. Beware: g2 is translated below!
    //if (tileOriginX > g2.getClip().getBounds2D().getMaxX()) {
    //  return;
    //}
    //if (tileMaxX < g2.getClip().getBounds2D().getMinX()) {
    //  return;
    //}
    //if (tileOriginY > g2.getClip().getBounds2D().getMaxY()) {
    //  return;
    //}
    //if (tileMaxY < g2.getClip().getBounds2D().getMinY()) {
    //  return;
    //}

    Graphics2D copy = (Graphics2D) g2.create();
    try {
      copy.translate(tileOriginX, tileOriginY);

      Tile tile = getTilesProvider().getTile(new TileLocation(tileX, tileY, zoomFactorX, zoomFactorY, tilesWidth, tilesHeight));
      drawTile(copy, tile);
    } finally {
      copy.dispose();
    }
  }

  /**
   * @noinspection MethodMayBeStatic
   */
  protected void drawTile(@Nonnull Graphics2D g2d, @Nonnull Tile tile) {
    tile.draw(g2d, 0, 0);
  }

  @Nonnull
  public TilesProvider getTilesProvider() {
    return tilesProvider;
  }

  @Nonnull
  public FramePerSecondsCalculator getFramePerSecondsCalculator() {
    return framePerSecondsCalculator;
  }

  @Nonnull
  public Converter getConverter() {
    return converter;
  }

  @UiThread
  @View
  @px
  @Nonnull
  public Point2D.Double getViewMovement() {
    return viewMovement;
  }

  @UiThread
  public double getZoomFactorX() {
    return zoomFactorX;
  }

  @UiThread
  public double getZoomFactorY() {
    return zoomFactorY;
  }

  @UiThread
  @Nonnull
  public VisibleTiles getVisibleTiles() {
    return visibleTiles;
  }

  @Deprecated
  public double findValidZoomFactorBelow(double requestedZoomFactor) {
    for (int zoomLevel = maxZoomLevel; zoomLevel >= MIN_ZOOM_LEVEL; zoomLevel--) {
      double zoomFactor = zoomLevel2Factor(zoomLevel);
      if (zoomFactor <= requestedZoomFactor) {
        return zoomFactor;
      }
    }

    //fallback
    return MIN_ZOOM_FACTOR;
  }

  /**
   * May be registered and is called every frame
   */
  @FunctionalInterface
  public interface TimedUpdater {
    /**
     * Is called every time before the component is repainted.
     * Can be used for ongoing changes that should be calculated on every repaint (e.g. movement or other animations)
     */
    @UiThread
    void update(@Nonnull TilesComponent tilesComponent);
  }

  /**
   * Abstract base class that calculates the elapsed time since last call to update
   */
  public abstract static class AbstractTimedUpdater implements TimedUpdater {
    @ns
    protected long lastUpdate = System.nanoTime();

    @UiThread
    @Override
    public void update(@Nonnull TilesComponent tilesComponent) {
      @ns long now = System.nanoTime();
      @ns long elapsedSinceLastUpdate = now - lastUpdate;
      lastUpdate = now;

      update(tilesComponent, now, elapsedSinceLastUpdate);
    }

    @UiThread
    protected abstract void update(@Nonnull TilesComponent tilesComponent, @ns long now, @ns long deltaSinceLastUpdate);
  }

  @Nonnull
  private final List<ZoomFactorListener> zoomFactorListeners = new CopyOnWriteArrayList<>();

  public void addListener(@Nonnull ZoomFactorListener zoomFactorListener) {
    this.zoomFactorListeners.add(zoomFactorListener);
  }

  public void removeListener(@Nonnull ZoomFactorListener zoomFactorListener) {
    this.zoomFactorListeners.remove(zoomFactorListener);
  }

  @UiThread
  private void notifyZoomFactorUpdated(double oldZoomFactorX, double oldZoomFactorY, double newZoomFactorX, double newZoomFactorY) {
    for (ZoomFactorListener zoomFactorListener : zoomFactorListeners) {
      zoomFactorListener.zoomFactorUpdated(oldZoomFactorX, oldZoomFactorY, newZoomFactorX, newZoomFactorY);
    }
  }

  /**
   * Listener that is notified whenever the zoom factor has been updated
   */
  @FunctionalInterface
  public interface ZoomFactorListener {
    @UiThread
    void zoomFactorUpdated(double oldZoomFactorX, double oldZoomFactorY, double newZoomFactorX, double newZoomFactorY);
  }
}
