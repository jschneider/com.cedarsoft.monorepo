package com.cedarsoft.commons.javafx.busy;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

/**
 * Contains code that manages the busy state.
 *
 * Listens to changes of the given busy state.
 * To detect cases in which the busy state has already been set to false before {@link #registerCallback(FinishedCallback)} has
 * been called, an additional check is performed after {@link #defaultManualCheckDelay}.
 *
 * If the busy state is still false after {@link #defaultTimeoutDelay}, the callback is notified about the timeout
 */
public class BusySupport {
  @Nonnull
  private final ReadOnlyBooleanProperty busyStatusProperty;
  /**
   * The delay when the first manual check is done.
   */
  @Nonnull
  private final Duration defaultManualCheckDelay;
  @Nonnull
  private final Duration defaultTimeoutDelay;

  public BusySupport(@Nonnull ReadOnlyBooleanProperty busyStatusProperty) {
    this(busyStatusProperty, Duration.millis(500), Duration.seconds(30));
  }

  public BusySupport(@Nonnull ReadOnlyBooleanProperty busyStatusProperty, @Nonnull Duration defaultManualCheckDelay, @Nonnull Duration defaultTimeoutDelay) {
    this.busyStatusProperty = busyStatusProperty;
    this.defaultManualCheckDelay = defaultManualCheckDelay;
    this.defaultTimeoutDelay = defaultTimeoutDelay;
  }

  public boolean isBusyStatusProperty() {
    return busyStatusProperty.get();
  }

  @Nonnull
  public ReadOnlyBooleanProperty busyStatusPropertyProperty() {
    return busyStatusProperty;
  }

  @Nonnull
  public Duration getDefaultManualCheckDelay() {
    return defaultManualCheckDelay;
  }

  @Nonnull
  public Duration getDefaultTimeoutDelay() {
    return defaultTimeoutDelay;
  }

  /**
   * Calls the callback as soon as the busy status is false.
   * A manual check is performed after {@link #defaultManualCheckDelay} to detect cases where the busy state has already been set to false
   */
  public void registerCallback(@Nonnull FinishedCallback callback) {
    registerCallback(callback, defaultManualCheckDelay, defaultTimeoutDelay);
  }

  public void registerCallback(@Nonnull FinishedCallback callback, @Nonnull Duration manualCheckDelay, @Nonnull Duration timeoutDelay) {
    //Listener for a change
    ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (oldValue == newValue) {
          //Avoid duplicate notifications
          return;
        }

        if (!newValue) {
          //Remove the listener first to avoid multiple calls (if the callback itself sets busy to true again
          observable.removeListener(this);
          callback.finished();
        }
      }
    };
    busyStatusProperty.addListener(listener);

    AtomicBoolean finished = new AtomicBoolean();

    //Check after (default: 500 millis) to get very fast events
    new Timeline(new KeyFrame(manualCheckDelay, event -> {
      if (!busyStatusProperty.get()) {
        finished.set(true);
        //Remove the listener first to avoid multiple calls (if the callback itself sets busy to true again
        busyStatusProperty.removeListener(listener);
        callback.finished();
      }
    })).play();

    //Timeout check
    new Timeline(new KeyFrame(timeoutDelay, event -> {
      if (finished.get()) {
        //Already notified, just return
        return;
      }

      if (!busyStatusProperty.get()) {
        finished.set(true);
        busyStatusProperty.removeListener(listener);
        callback.finished();
      }
      else {
        //Still busy, timeout
        finished.set(true);
        busyStatusProperty.removeListener(listener);
        callback.timedOut();
      }
    })).play();
  }

  /**
   * Creates a busy support that returns after a given timeout
   */
  @Nonnull
  public static BusySupport createTimed(int seconds) {
    return new BusySupport(new SimpleBooleanProperty(true), Duration.millis(500), Duration.seconds(seconds));
  }
}
