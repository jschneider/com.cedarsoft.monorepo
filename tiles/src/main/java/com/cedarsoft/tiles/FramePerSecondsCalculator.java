package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.si.ms;
import com.cedarsoft.unit.si.ns;

/**
 * Helper class that calculates how many frames per second have happened
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FramePerSecondsCalculator {
  /**
   * Contains the time of the last frame
   */
  @ns
  private long lastFrame;
  /**
   * Contains the last delta between frames
   */
  @ns
  private long lastDelta;

  /**
   * Is called every time a new frame is painted
   */
  @UiThread
  public void newFrame() {
    @ns long now = System.nanoTime();
    lastDelta = now - lastFrame;
    lastFrame = now;
  }

  @ns
  public long getLastDelta() {
    return lastDelta;
  }

  /**
   * Returns the frame per second
   */
  public double getLastFps() {
    return 1000.0 / lastDeltaMillis();
  }

  @ms
  private double lastDeltaMillis() {
    return lastDelta / (1000.0 * 1000.0);
  }
}
