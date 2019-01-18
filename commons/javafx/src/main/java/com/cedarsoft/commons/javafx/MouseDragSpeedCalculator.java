package com.cedarsoft.commons.javafx;

import java.util.Collection;
import java.util.Collections;
import java.util.function.ToDoubleFunction;

import javax.annotation.Nonnull;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.other.px_ns;
import com.cedarsoft.unit.si.ns;
import com.google.common.collect.EvictingQueue;

/**
 * Calculates mouse speeds when dragging
 */
public class MouseDragSpeedCalculator {
  public static final int SPEED_QUEUE_SIZE = 15;
  @ns
  public static final int MAX_AGE = 400 * 1000 * 1000;

  /**
   * Contains the last speed elements of the last n dragging events
   */
  @px_ns
  @Nonnull
  @UiThread
  private final EvictingQueue<Entry> entries = EvictingQueue.create(SPEED_QUEUE_SIZE);

  @px_ns
  @UiThread
  private double calculateSpeed(@Nonnull ToDoubleFunction<Entry> getSpeed) {
    if (entries.isEmpty()) {
      return 0;
    }

    // The algorithm is taken from http://mortoray.com/2015/04/08/measuring-finger-mouse-velocity-at-release-time/ .
    double totalTime = 0.0;
    for (Entry entry : entries) {
      totalTime += entry.getDeltaTime();
    }

    double speed = 0.0;
    for (Entry entry : entries) {
      double alpha = 1.0 - Math.pow(Math.E, -entry.getDeltaTime() / totalTime);
      speed = alpha * getSpeed.applyAsDouble(entry) + (1.0 - alpha) * speed;
    }
    return speed;
  }

  @px_ns
  @UiThread
  public double calculateSpeedX() {
    return calculateSpeed(Entry::getSpeedX);
  }

  @px_ns
  @UiThread
  public double calculateSpeedY() {
    return calculateSpeed(Entry::getSpeedY);
  }

  /**
   * Adds a new entry
   *
   * @param deltaTime the delta time
   */
  @UiThread
  public void add(@ns long deltaTime, @px double deltaX, @px double deltaY) {
    if (deltaTime <= 0) {
      return;
    }

    @ns long now = System.nanoTime();

    //first remove entries that might be too old
    entries.removeIf(entry -> now - entry.getTime() > MAX_AGE);

    entries.add(new Entry(now, deltaTime, deltaX / (double) deltaTime, deltaY / (double) deltaTime));
  }

  /**
   * Clears the speed calculation
   */
  @UiThread
  public void clear() {
    entries.clear();
  }

  @Nonnull
  public Collection<? extends Entry> getEntries() {
    return Collections.unmodifiableCollection(entries);
  }


  public static class Entry {
    @ns
    private final long time;
    @ns
    private final long deltaTime;
    @px_ns
    private final double speedX;
    @px_ns
    private final double speedY;

    public Entry(@ns long time, @ns long deltaTime, @px_ns double speedX, @px_ns double speedY) {
      this.time = time;
      this.deltaTime = deltaTime;
      this.speedX = speedX;
      this.speedY = speedY;
    }

    @ns
    public long getTime() {
      return time;
    }

    @ns
    public long getDeltaTime() {
      return deltaTime;
    }

    @px_ns
    public double getSpeedX() {
      return speedX;
    }

    @px_ns
    public double getSpeedY() {
      return speedY;
    }

    @Override
    public String toString() {
      return String.format("Entry{time=%d, deltaTime=%d, speedX=%s, speedY=%s}", time, deltaTime, speedX, speedY);
    }
  }
}
