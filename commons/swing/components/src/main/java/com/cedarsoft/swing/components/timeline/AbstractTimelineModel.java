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
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
