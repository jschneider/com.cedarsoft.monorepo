package com.cedarsoft.tiles.feature;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.tiles.MovementSpeedCalculator;
import com.cedarsoft.tiles.TilesComponent;
import com.cedarsoft.tiles.VisibleArea;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.other.px_ns;
import com.cedarsoft.unit.si.ms;
import com.cedarsoft.unit.si.ns;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class PanningByDragFeature extends AbstractDragFeature implements TilesComponent.TimedUpdater {
  @px
  public static final int DEAD_ZONE_SIZE = 50;
  @ms
  public static final int MAX_DRAG_RELEASE_DELTA = 100;

  /**
   * The center(s) of the dead zone.
   * Only if the dead zone is left, the movement begins.
   * <p>
   * If the center is null it is no longer relevant.
   */
  @VisibleArea
  @px
  @Nullable
  protected Integer deadZoneCenterX;
  @VisibleArea
  @px
  @Nullable
  protected Integer deadZoneCenterY;

  @UiThread
  @Nonnull
  private final MovementSpeedCalculator speedCalculator = new MovementSpeedCalculator();

  @Nonnull
  private final DelayedMovementTimedUpdater delayedMovementCalculator = new DelayedMovementTimedUpdater();
  @Nonnull
  private final TilesComponent tilesComponent;

  @Nonnull
  private final InertialScrollingSpeedModifier inertialScrollingSpeedModifier;


  PanningByDragFeature(@Nonnull TilesComponent tilesComponent, @Nonnull InertialScrollingSpeedModifier inertialScrollingSpeedModifier) {
    this.tilesComponent = tilesComponent;
    this.inertialScrollingSpeedModifier = inertialScrollingSpeedModifier;
  }

  @UiThread
  @Override
  public void mousePressed(MouseEvent e) {
    if (!SwingUtilities.isLeftMouseButton(e)) {
      return;
    }

    super.mousePressed(e);

    delayedMovementCalculator.deactivate();

    speedCalculator.clear();

    deadZoneCenterX = e.getX();
    deadZoneCenterY = e.getY();
  }

  @UiThread
  @Override
  public void mouseDragged(@Nonnull MouseEvent e) {
    if (!SwingUtilities.isLeftMouseButton(e)) {
      return;
    }

    @ms long eventTime = e.getWhen();
    @ms long deltaTime = eventTime - lastEventTime;
    lastEventTime = eventTime;

    int relevantX = e.getX();
    int relevantY = e.getY();

    //dead zone for movements

    //clear the dead zones, if they no longer apply
    {
      if (deadZoneCenterX != null) {
        //Check if we have left the dead zone
        if (Math.abs(relevantX - deadZoneCenterX) > DEAD_ZONE_SIZE) {
          //noinspection ReuseOfLocalVariable
          deadZoneCenterX = null;
        }
      }
      if (deadZoneCenterY != null) {
        //Check if we have left the dead zone
        if (Math.abs(relevantY - deadZoneCenterY) > DEAD_ZONE_SIZE) {
          //noinspection ReuseOfLocalVariable
          deadZoneCenterY = null;
        }
      }
    }

    //Starting mode, we have both axis within the dead zone
    if (deadZoneCenterX != null && deadZoneCenterY != null) {
      @px int deltaFromInitialX = relevantX - deadZoneCenterX;
      @px int deltaFromInitialY = relevantY - deadZoneCenterY;

      //Both axis have a very small delta, so just skip
      if (Math.abs(deltaFromInitialX) < 5 && Math.abs(deltaFromInitialY) < 5) {
        return; //Just skip this event
      }

      @px int deltaXAbs = Math.abs(deltaFromInitialX);
      @px int deltaYAbs = Math.abs(deltaFromInitialY);

      if (deltaXAbs > deltaYAbs + 10) {
        //Delta X is larger
        //Clear the dead zone
        deadZoneCenterX = null;
        //We start dragging from here
        lastDragEventX = relevantX;
      }
      else if (deltaYAbs > deltaXAbs + 10) {
        //Delta y is larger
        //Clear the dead zone
        deadZoneCenterY = null;
        //We start dragging from here
        lastDragEventY = relevantY;
      }
      else if (deltaXAbs + deltaYAbs > DEAD_ZONE_SIZE) {
        //Both are wide off, clear both dead zones
        deadZoneCenterX = null;
        lastDragEventX = relevantX;

        deadZoneCenterY = null;
        lastDragEventY = relevantY;
      }
      else {
        //no delta is much larger, so just skip this event
        return;
      }
    }
    else {
      //just one dead zone may still be active (or none).
      //So we have to reset the value for the remaining dead zone
      if (deadZoneCenterX != null) {
        //we reset one of the dead zones
        if (Math.abs(relevantX - deadZoneCenterX) < DEAD_ZONE_SIZE) {
          //noinspection ReuseOfLocalVariable
          relevantX = deadZoneCenterX;
        }
      }

      if (deadZoneCenterY != null) {
        if (Math.abs(relevantY - deadZoneCenterY) < DEAD_ZONE_SIZE) {
          //noinspection ReuseOfLocalVariable
          relevantY = deadZoneCenterY;
        }
      }
    }

    @px int deltaX = relevantX - lastDragEventX;
    @px int deltaY = relevantY - lastDragEventY;


    tilesComponent.moveView(deltaX, deltaY);

    speedCalculator.add(deltaX, deltaY, deltaTime, eventTime);

    lastDragEventX = relevantX;
    lastDragEventY = relevantY;
  }

  @UiThread
  @Override
  public void mouseReleased(MouseEvent e) {
    if (!SwingUtilities.isLeftMouseButton(e)) {
      return;
    }

    @ms long deltaMillis = e.getWhen() - lastEventTime;
    if (deltaMillis > MAX_DRAG_RELEASE_DELTA) {
      speedCalculator.clear();
      return;
    }


    delayedMovementCalculator.prepare();
    delayedMovementCalculator.activate();

    deadZoneCenterX = null;
    deadZoneCenterY = null;

    super.mouseReleased(e);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
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
  public void update(@Nonnull TilesComponent tilesComponent) {
    delayedMovementCalculator.update(tilesComponent);
  }

  @Nonnull
  public MovementSpeedCalculator getSpeedCalculator() {
    return speedCalculator;
  }

  /**
   * Installs the feature to the given tiles component
   *
   * @param tilesComponent the tiles component the feature is added to
   */
  @Nonnull
  public static PanningByDragFeature install(@Nonnull TilesComponent tilesComponent, @Nonnull InertialScrollingSpeedModifier inertialScrollingSpeedModifier) {
    PanningByDragFeature panningByDragFeature = new PanningByDragFeature(tilesComponent, inertialScrollingSpeedModifier);
    tilesComponent.addUpdater(panningByDragFeature);
    tilesComponent.addMouseListener(panningByDragFeature);
    tilesComponent.addMouseMotionListener(panningByDragFeature);

    return panningByDragFeature;
  }

  @UiThread
  public void destroy() {
    tilesComponent.removeUpdater(this);
    tilesComponent.removeMouseListener(this);
    tilesComponent.removeMouseMotionListener(this);
  }

  private class DelayedMovementTimedUpdater extends TilesComponent.AbstractTimedUpdater {
    @ns
    public static final int TARGET_TIME = 1000 * 1000 * 1000;

    @ns
    private long firstRun;
    private double factor = 1;

    public void prepare() {
      lastUpdate = System.nanoTime();
      firstRun = lastUpdate;
      factor = 1;
    }

    @Override
    @UiThread
    protected void update(@Nonnull TilesComponent tilesComponent, @ns long now, @ns long deltaSinceLastUpdate) {
      if (!isActive()) {
        return;
      }

      @ns long deltaSinceStart = now - firstRun;
      factor = ((double) TARGET_TIME - deltaSinceStart) / TARGET_TIME;

      // the speed calculator provides px/ms; what we need is px/ns
      @px_ns final double speedX = speedCalculator.calculateSpeedX() * 0.000_001;
      @px_ns final double speedY = speedCalculator.calculateSpeedY() * 0.000_001;

      @px double moveX = inertialScrollingSpeedModifier.calculateSpeedX(speedX) * factor * deltaSinceLastUpdate;
      @px double moveY = inertialScrollingSpeedModifier.calculateSpeedY(speedY) * factor * deltaSinceLastUpdate;
      if (factor < 0 || Math.abs(moveX) < 0.1 && Math.abs(moveY) < 0.1) {
        active = false;
        return;
      }

      tilesComponent.moveView(moveX, moveY);
    }

    private boolean active;

    @UiThread
    public void activate() {
      active = true;
    }

    @UiThread
    public boolean isActive() {
      return active;
    }

    @UiThread
    public void deactivate() {
      active = false;
    }
  }

}
