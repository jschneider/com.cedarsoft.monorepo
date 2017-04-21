package com.cedarsoft.tiles;

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.px;
import com.cedarsoft.unit.other.px_ms;
import com.cedarsoft.unit.si.ms;
import com.google.common.collect.EvictingQueue;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.function.ToDoubleFunction;

/**
 * Can be used to calculate the speed of drag events
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MovementSpeedCalculator {
  /**
   * The max age for each event. Older values are discarded
   */
  @ms
  public static final long MAX_AGE = 500;

  @px_ms
  @Nonnull
  @UiThread
  private final EvictingQueue<Movement> movements = EvictingQueue.create(18);

  @px_ms
  @UiThread
  private double calculateSpeed(@Nonnull ToDoubleFunction<Movement> getSpeed) {
    if (movements.isEmpty()) {
      return 0;
    }

    // The algorithm is taken from http://mortoray.com/2015/04/08/measuring-finger-mouse-velocity-at-release-time/ .
    double totalTime = 0.0;
    for (Movement movement : movements) {
      totalTime += movement.getDeltaTime();
    }

    double speed = 0.0;
    for (Movement movement : movements) {
      double alpha = 1.0 - Math.pow(Math.E, -movement.getDeltaTime() / totalTime);
      speed = alpha * getSpeed.applyAsDouble(movement) + (1.0 - alpha) * speed;
    }
    return speed;
  }

  @px_ms
  @UiThread
  public double calculateSpeedX() {
    return calculateSpeed(Movement::getSpeedX);
  }

  @px_ms
  @UiThread
  public double calculateSpeedY() {
    return calculateSpeed(Movement::getSpeedY);
  }

  /**
   * Adds a new movement
   */
  @UiThread
  public void add(@px int movementX, @px int movementY, @ms long deltaTime, @ms long time) {
    if (deltaTime <= 0) {
      return;
    }
    removeExpiredMovements();
    movements.add(new Movement(time, deltaTime, movementX / (double) deltaTime, movementY / (double) deltaTime));
  }

  @UiThread
  private void removeExpiredMovements() {
    @ms long now = System.currentTimeMillis();
    //delete movements that have expired
    movements.removeIf(movement -> now - movement.getTime() > MAX_AGE || movement.getTime() > now);
  }

  /**
   * Clears all movements
   */
  @UiThread
  public void clear() {
    movements.clear();
  }

  @UiThread
  @Nonnull
  public Collection<? extends Movement> getMovements() {
    return Collections.unmodifiableCollection(movements);
  }

  /**
   * Describes a mouse movement
   */
  public static class Movement {
    /**
     * The date when the movement has finished
     */
    @ms
    private final long time;
    @ms
    private final long deltaTime;
    @px_ms
    private final double speedX;
    @px_ms
    private final double speedY;

    public Movement(@ms long time, @ms long deltaTime, @px_ms double speedX, @px_ms double speedY) {
      this.time = time;
      this.deltaTime = deltaTime;
      this.speedX = speedX;
      this.speedY = speedY;
    }

    @ms
    public long getTime() {
      return time;
    }

    @ms
    public long getDeltaTime() {
      return deltaTime;
    }

    @px_ms
    public double getSpeedX() {
      return speedX;
    }

    @px_ms
    public double getSpeedY() {
      return speedY;
    }

    @Override
    public String toString() {
      return String.format("Entry{time=%d, deltaTime=%d, speedX=%s, speedY=%s}", time, deltaTime, speedX, speedY);
    }
  }
}
