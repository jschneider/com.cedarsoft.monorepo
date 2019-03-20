package com.cedarsoft.commons.javafx;

import javax.annotation.Nonnull;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


/**
 * A class for timers executed on the JavaFX Application Thread.
 *
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class JavaFxTimer {

  private JavaFxTimer() {
    // utility class
  }

  /**
   * Calls the given runnable on the JavaFX Application Thread after the given delay.
   */
  public static void start(@Nonnull Duration delay, @Nonnull final Runnable run) {
    new Timeline(new KeyFrame(delay, ae -> run.run())).play();
  }

  public static void repeat(@Nonnull Duration delay, @Nonnull final Runnable run) {
    Timeline timeline = new Timeline(new KeyFrame(delay, ae -> run.run()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

}
