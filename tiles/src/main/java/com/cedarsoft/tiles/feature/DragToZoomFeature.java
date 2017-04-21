package com.cedarsoft.tiles.feature;

import com.cedarsoft.swing.common.ColorTools;
import com.cedarsoft.tiles.Overlay;
import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.View;
import com.cedarsoft.tiles.VisibleArea;
import com.cedarsoft.unit.other.px;
import com.jidesoft.plaf.UIDefaultsLookup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Zooms on drag events
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DragToZoomFeature extends AbstractDragFeature implements Overlay {
  @Nonnull
  private final TilesComponent tilesComponent;
  @Nonnull
  private final Color selectionColor = ColorTools.withAlpha(UIDefaultsLookup.getColor("Table.selectionBackground"), 0.3);

  private DragToZoomFeature(@Nonnull TilesComponent tilesComponent) {
    this.tilesComponent = tilesComponent;
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (!SwingUtilities.isRightMouseButton(e)) {
      return;
    }

    super.mousePressed(e);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (!SwingUtilities.isRightMouseButton(e)) {
      return;
    }

    super.mouseDragged(e);
    tilesComponent.markAsDirty();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (!SwingUtilities.isRightMouseButton(e)) {
      return;
    }

    super.mouseReleased(e);

    @VisibleArea @px int minX = Math.min(this.initialPressedEventX, e.getX());
    @VisibleArea @px int minY = Math.min(this.initialPressedEventY, e.getY());

    @VisibleArea @px double width = Math.abs(e.getX() - this.initialPressedEventX);
    @VisibleArea @px double height = Math.abs(e.getY() - this.initialPressedEventY);

    //TODO change with AND?
    if (width < 2 || height < 2) {
      return;
    }

    @px @VisibleArea Point2D.Double zoomLocation = new Point2D.Double(minX + width / 2.0, minY + height / 2.0);

    @View double zoomLocationX = tilesComponent.getConverter().visibleArea2ViewX(zoomLocation.x);
    @View double zoomLocationY = tilesComponent.getConverter().visibleArea2ViewY(zoomLocation.y);

    double newZoomFactorX = tilesComponent.getWidth() / tilesComponent.getConverter().view2ModelX(width);
    double newZoomFactorY = tilesComponent.getHeight() / tilesComponent.getConverter().view2ModelY(height);

    tilesComponent.setZoomFactor(newZoomFactorX, newZoomFactorY, zoomLocation);
    tilesComponent.centerViewTo(-zoomLocationX, -zoomLocationY);

    for (Listener listener : listeners) {
      listener.zoomed(this, tilesComponent, newZoomFactorX, newZoomFactorY);
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  @Override
  public void paint(@Nonnull Graphics2D g2d, @Nonnull TilesComponent tilesComponent) {
    //We are currently not dragging
    if (!isButtonDown) {
      return;
    }

    g2d.setColor(selectionColor);

    @VisibleArea @px int minX = Math.min(this.initialPressedEventX, lastDragEventX);
    @VisibleArea @px int minY = Math.min(this.initialPressedEventY, lastDragEventY);
    @VisibleArea @px int width = Math.abs(lastDragEventX - this.initialPressedEventX);
    @VisibleArea @px int height = Math.abs(lastDragEventY - this.initialPressedEventY);

    Rectangle2D.Double rect = new Rectangle2D.Double(minX, minY, width, height);
    g2d.fill(rect);
  }

  @Nonnull
  private final List<Listener> listeners = new CopyOnWriteArrayList<>();

  public void addListener(@Nonnull Listener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(@Nonnull Listener listener) {
    this.listeners.remove(listener);
  }

  @FunctionalInterface
  public interface Listener {
    void zoomed(@Nonnull DragToZoomFeature feature, @Nonnull TilesComponent source, double newZoomFactorX, double newZoomFactorY);
  }

  public static void install(@Nonnull TilesComponent tilesComponent) {
    install(tilesComponent, null);
  }

  public static void install(@Nonnull TilesComponent tilesComponent, @Nullable Listener listener) {
    DragToZoomFeature dragToZoomFeature = new DragToZoomFeature(tilesComponent);

    tilesComponent.addMouseMotionListener(dragToZoomFeature);
    tilesComponent.addMouseListener(dragToZoomFeature);
    tilesComponent.addOverlay(dragToZoomFeature);

    if (listener != null) {
      dragToZoomFeature.addListener(listener);
    }
  }
}
