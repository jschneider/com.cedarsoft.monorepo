package com.cedarsoft.swing.components.timeline;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

public class BarValuesBuilder {
  @Nonnull
  private final LocalDate startDate;
  @Nonnull
  private final LocalDate endDate;

  @Nonnull
  private final List<ConstantBarValues.Builder> barValuesBuilders = new ArrayList<>();

  public BarValuesBuilder(@Nonnull LocalDate startDate, @Nonnull LocalDate endDate) throws InconsistencyException {
    this.startDate = startDate;
    this.endDate = endDate;

    int days = (int) (ChronoUnit.DAYS.between(startDate, endDate) + 1);

    if (days <= 0) {
      throw new InconsistencyException("Invalid dates <" + startDate + " - " + endDate + ">");
    }

    //Populate the list initially
    for (int i = 0; i < days; i++) {
      barValuesBuilders.add(new ConstantBarValues.Builder());
    }

    if (barValuesBuilders.isEmpty()) {
      throw new InconsistencyException("empty <" + days + ">");
    }
  }

  @Nonnull
  public LocalDate getStartDate() {
    return startDate;
  }

  @Nonnull
  public LocalDate getEndDate() {
    return endDate;
  }

  public void add(@Nonnull LocalDate date, @Nonnull TimelineModel.Severity severity) throws InconsistencyException {
    try {
      int index = (int) ChronoUnit.DAYS.between(startDate, date);
      barValuesBuilders.get(index).increase(severity);
    } catch (IndexOutOfBoundsException e) {
      throw new InconsistencyException("Invalid date <" + date + "> for start date <" + startDate + "> with length <" + barValuesBuilders.size() + ">", e);
    }
  }

  @Nonnull
  public List<? extends TimelineModel.BarValues> build() {
    if (barValuesBuilders.isEmpty()) {
      throw new IllegalStateException("no bar values builders found");
    }

    return barValuesBuilders
      .stream()
      .map(ConstantBarValues.Builder::build)
      .collect(Collectors.toList());
  }

  public static class InconsistencyException extends Exception {
    public InconsistencyException(String message) {
      super(message);
    }

    public InconsistencyException(@Nonnull String message, @Nonnull Throwable cause) {
      super(message, cause);
    }
  }
}
