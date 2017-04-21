package com.cedarsoft.tiles.feature;

import com.cedarsoft.tiles.VisibleArea;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.si.ms;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Abstract base class for drag related features
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractDragFeature implements MouseMotionListener, MouseListener {
  /**
   * The time when the last event happened
   */
  @ms
  protected long lastEventTime;
  /**
   * The location of the last drag event
   */
  @VisibleArea
  @px
  protected int lastDragEventX;
  /**
   * The location of the last drag event
   */
  @VisibleArea
  @px
  protected int lastDragEventY;

  /**
   * The location of the initial press event
   */
  @VisibleArea
  @px
  protected int initialPressedEventX;
  /**
   * The location of the initial press event
   */
  @VisibleArea
  @px
  protected int initialPressedEventY;

  /**
   * The location of the release event
   */
  @VisibleArea
  @px
  protected int releasedEventX;
  /**
   * The location of the release event
   */
  @VisibleArea
  @px
  protected int releasedEventY;

  /**
   * Is set to true if the mouse button is currently pressed
   */
  protected boolean isButtonDown;

  @Override
  public void mousePressed(MouseEvent e) {
    lastEventTime = e.getWhen();

    lastDragEventX = e.getX();
    lastDragEventY = e.getY();

    initialPressedEventX = e.getX();
    initialPressedEventY = e.getY();

    isButtonDown = true;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    lastEventTime = e.getWhen();

    lastDragEventX = e.getX();
    lastDragEventY = e.getY();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    lastEventTime = e.getWhen();

    releasedEventX = e.getX();
    releasedEventY = e.getY();

    isButtonDown = false;
  }
}
