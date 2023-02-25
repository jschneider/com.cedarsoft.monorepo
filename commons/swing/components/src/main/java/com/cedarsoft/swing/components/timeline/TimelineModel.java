/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.swing.components.timeline;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import it.neckar.open.annotations.UiThread;

/**
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
