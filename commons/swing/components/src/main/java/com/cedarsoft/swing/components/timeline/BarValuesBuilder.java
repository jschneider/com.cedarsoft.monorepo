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
