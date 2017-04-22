package com.cedarsoft.swing.components.timeline;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.annotations.UiThread;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public interface TimelineModel {
  /**
   * Returns the amount of bars
   */
  @UiThread
  int getBarsCount();

  @UiThread
  @Nonnull
  BarValues getBarValues(int index);

  @UiThread
  @Nonnull
  String getLabel(int index);

  @UiThread
  @Nullable
  VisibleArea getVisibleArea();

  void addListener(@Nonnull Listener listener);

  void removeListener(@Nonnull Listener listener);

  boolean isValid();

  interface Listener {
    @UiThread
    void modelChanged(@Nonnull TimelineModel model);

    @UiThread
    void visibleAreaUpdated(@Nonnull TimelineModel model, @Nullable VisibleArea visibleArea);
  }

  /**
   * The visible area
   */
  class VisibleArea {
    private final int upper;
    private final int lower;

    public VisibleArea(int lower, int upper) {
      if (lower > upper) {
        throw new IllegalArgumentException("Lower <" + lower + "> must not be larger than <" + upper + ">");
      }

      this.upper = upper;
      this.lower = lower;
    }

    public int getUpper() {
      return upper;
    }

    public int getLower() {
      return lower;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof VisibleArea)) {
        return false;
      }

      VisibleArea that = (VisibleArea) obj;

      if (lower != that.lower) {
        return false;
      }
      return upper == that.upper;
    }

    @Override
    public int hashCode() {
      int result = upper;
      result = 31 * result + lower;
      return result;
    }

    @Override
    public String toString() {
      return "VisibleArea{" + "top=" + upper + ", bottom=" + lower + '}';
    }
  }

  /**
   * Contains the values for one bar
   */
  interface BarValues {
    double getValueCritical();

    double getValueWarnings();

    double getValueNeutral();
  }

  enum Severity {
    CRITICAL, WARNING, NEUTRAL
  }
}
