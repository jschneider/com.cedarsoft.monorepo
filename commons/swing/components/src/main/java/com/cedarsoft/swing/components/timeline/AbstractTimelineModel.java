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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.annotations.UiThread;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

/**
 */
public abstract class AbstractTimelineModel implements TimelineModel {
  @Nonnull
  private LocalDate startDate = LocalDate.now();

  @Nonnull
  private List<? extends BarValues> barValues = Collections.emptyList();

  @Nullable
  private VisibleArea visibleArea;

  private boolean valid;

  @UiThread
  public void update(@Nonnull LocalDate startDate, @Nonnull List<? extends BarValues> barValues) {
    this.startDate = startDate;
    this.barValues = ImmutableList.copyOf(barValues);
    this.valid = true;
    notifyModelChanged();
  }

  protected void setValid(boolean valid) {
    this.valid = valid;
  }

  @Override
  public boolean isValid() {
    return valid;
  }

  @Override
  @Nullable
  @UiThread
  public VisibleArea getVisibleArea() {
    return visibleArea;
  }

  @UiThread
  public void setVisibleArea(@Nullable VisibleArea visibleArea) {
    if (Objects.equal(this.visibleArea, visibleArea)) {
      return;
    }

    this.visibleArea = visibleArea;

    notifyVisibleAreaUpdated(visibleArea);
  }

  @UiThread
  protected void notifyVisibleAreaUpdated(@Nullable VisibleArea visibleArea) {
    for (Listener listener : listeners) {
      listener.visibleAreaUpdated(this, visibleArea);
    }
  }

  @UiThread
  protected void notifyModelChanged() {
    for (Listener listener : listeners) {
      listener.modelChanged(this);
    }
  }

  @Nonnull
  public LocalDate getStartDate() {
    return startDate;
  }

  @Nonnull
  public LocalDate getHighestDate() {
    return startDate.plusDays(barValues.size());
  }

  @Override
  public int getBarsCount() {
    return barValues.size();
  }

  /**
   * Returns the bar values for the given day
   */
  @Nonnull
  @Override
  public BarValues getBarValues(int index) {
    return barValues.get(index);
  }

  @UiThread
  @Nonnull
  private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

  @Nonnull
  @Override
  @UiThread
  public String getLabel(int index) {
    LocalDate localDate = startDate.plusDays(index);
    return formatter.format(localDate);
  }

  @Nonnull
  private final List<Listener> listeners = new CopyOnWriteArrayList<>();

  @Override
  public void addListener(@Nonnull Listener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void removeListener(@Nonnull Listener listener) {
    this.listeners.remove(listener);
  }
}
