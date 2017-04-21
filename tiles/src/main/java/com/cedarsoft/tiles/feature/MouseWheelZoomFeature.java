package com.cedarsoft.tiles.feature;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.VisibleArea;
import com.cedarsoft.tiles.ZoomModifier;
import com.cedarsoft.unit.other.px;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

/**
 *
 */
public class MouseWheelZoomFeature implements MouseWheelListener {
  @Nonnull
  private final TilesComponent tilesComponent;
  @Nullable
  private final DeadZoneProvider deadZoneProvider;
  @Nonnull
  private final ZoomModifier zoomModifier;

  private MouseWheelZoomFeature(@Nonnull TilesComponent tilesComponent, @Nullable DeadZoneProvider deadZoneProvider, @Nonnull ZoomModifier zoomModifier) {
    this.zoomModifier = zoomModifier;
    this.tilesComponent = tilesComponent;
    this.deadZoneProvider = deadZoneProvider;
  }

  @Override
  @UiThread
  public void mouseWheelMoved(MouseWheelEvent e) {
    Point mouseWheelLocation = e.getPoint();

    int zoomAmountX = zoomModifier.getZoomX(e.getWheelRotation(), e.isControlDown(), e.isShiftDown());
    int zoomAmountY = zoomModifier.getZoomY(e.getWheelRotation(), e.isControlDown(), e.isShiftDown());

    tilesComponent.zoomView(zoomAmountX, zoomAmountY, processDeadZone(mouseWheelLocation));
  }

  /**
   * Process the dead zone
   */
  @UiThread
  @Nonnull
  private Point2D processDeadZone(@Nonnull Point mouseWheelLocation) {
    if (deadZoneProvider == null) {
      return mouseWheelLocation;
    }

    @VisibleArea @px int x = mouseWheelLocation.x;
    @px @VisibleArea double deadZoneCenterX = deadZoneProvider.getDeadZoneCenterX();

    if (Math.abs(deadZoneCenterX - x) < 30) {
      //We have mouse wheel near the dead zone center
      return new Point2D.Double(deadZoneCenterX, mouseWheelLocation.y);
    }

    return mouseWheelLocation;
  }

  public static void install(@Nonnull TilesComponent tilesComponent) {
    install(tilesComponent, null, ZoomModifier.NONE);
  }

  public static void install(@Nonnull TilesComponent tilesComponent, @Nullable DeadZoneProvider deadZoneProvider, @Nonnull ZoomModifier zoomModifier) {
    MouseWheelZoomFeature mouseWheelZoomFeature = new MouseWheelZoomFeature(tilesComponent, deadZoneProvider, zoomModifier);
    tilesComponent.addMouseWheelListener(mouseWheelZoomFeature);
  }

  public interface DeadZoneProvider {
    /**
     * Returns the center of the dead zone
     */
    @px
    @VisibleArea
    @UiThread
    double getDeadZoneCenterX();
  }
}
