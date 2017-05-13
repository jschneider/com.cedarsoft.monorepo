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

package com.cedarsoft.business.calc;

import javax.annotation.Nonnull;
import org.joda.time.LocalDate;
import org.joda.time.PeriodType;

/**
 * Abstract implementation for time systems that are based on months and years with a fixed length.
 */
public abstract class EqualMonthYearTimeSystem implements TimeSystem {
  /**
   * The amount of months per year
   */
  public static final int MONTHS_PER_YEAR = 12;

  @Override
  public double calculateDays(@Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    org.joda.time.Period period = calculateRealPeriode( begin, end );
    return period.getYears() * getDaysPerYear() + period.getMonths() * getDaysPerMonth() + Math.min( getDaysPerMonth(), period.getDays() );
  }

  @Override
  public int calculateFullYears(@Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    return calculatePeriod( begin, end ).getYears();
  }

  @Override
  public double calculateYears(@Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    Period period = calculatePeriod( begin, end );

    double months = period.getMonths() + period.getDays() / getDaysPerMonth();
    return period.getYears() + months / MONTHS_PER_YEAR;
  }

  @Override
  @Nonnull
  public Period calculatePeriod( @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    org.joda.time.Period period = calculateRealPeriode( begin, end );
    return new Period( period.getYears(), period.getMonths(), ( int ) Math.min( getDaysPerMonth(), period.getDays() ) );
  }

  /**
   * Calculates the "real" period
   *
   * @param begin the begin
   * @param end   the end
   * @return the period (Year, Month, Day)
   */
  @Nonnull
  protected org.joda.time.Period calculateRealPeriode( LocalDate begin, LocalDate end ) {
    return new org.joda.time.Period( begin, end, PeriodType.yearMonthDay() );
  }

  /**
   * Returns the amount of days a month has
   *
   * @return the amount of days a month has
   */
  public abstract double getDaysPerMonth();

  /**
   * Returns the amount of days a year has
   *
   * @return the amount of days a year has
   */
  public abstract int getDaysPerYear();
}
