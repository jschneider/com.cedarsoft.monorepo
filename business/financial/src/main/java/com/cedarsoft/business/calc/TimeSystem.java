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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Calculates the times between two points in time.
 * The first
 */
public interface TimeSystem {
  /**
   * A time system that is based on the real calendar.
   */
  @Nonnull
  TimeSystem CALENDAR_BASED = new TimeSystem() {
    @Override
    public int calculateFullYears(@Nonnull LocalDate begin, @Nonnull LocalDate end ) {
      return calculatePeriod( begin, end ).getYears();
    }

    @Override
    @Nonnull
    public Period calculatePeriod( @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
      org.joda.time.Period period = new org.joda.time.Period( begin, end, PeriodType.yearMonthDay() );
      return new Period( period.getYears(), period.getMonths(), period.getDays() );
    }

    @Override
    public double calculateYears(@Nonnull LocalDate begin, @Nonnull LocalDate end ) {
      if ( begin.isEqual( end ) ) {
        return 0.0;
      }

      org.joda.time.Period period = new org.joda.time.Period( begin, end, PeriodType.yearDay() );

      //A period within the same year
      if ( begin.getYear() == end.getYear() ) {
        return ( double ) period.getDays() / ( double ) begin.dayOfYear().getMaximumValue();
      }

      double fractionOfFirstYear = calculateFractionOfYearTillEnd( begin );
      double fractionOfLastYear = calculateFractionOfYearSinceBegin( end );

      //period without the first and last year
      int midYears = calculateMiddleYears( begin, end );
      return fractionOfFirstYear + fractionOfLastYear + midYears;
    }

    /**
     * Calculates the amount of full years (starting and ending at 1.1.) between the two dates
     * @param begin
     * @param end
     * @return
     */
    private int calculateMiddleYears( @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
      LocalDate firstYearStart = begin.plusYears( 1 ).withDayOfYear( 1 );
      LocalDate lastYearEnd = end.withDayOfYear( 1 );

      org.joda.time.Period middleYears = new org.joda.time.Period( firstYearStart, lastYearEnd, PeriodType.yearDay() );
      return middleYears.getYears();
    }

    private double calculateFractionOfYearSinceBegin( @Nonnull LocalDate end ) {
      LocalDate begin = end.withDayOfYear( 1 );
      org.joda.time.Period period = new org.joda.time.Period( begin, end, PeriodType.days() );
      if ( period.getDays() == 0 ) {
        return 0.0;
      }
      return period.getDays() / ( double ) begin.dayOfYear().getMaximumValue();
    }

    private double calculateFractionOfYearTillEnd( @Nonnull LocalDate begin ) {
      LocalDate end = begin.plusYears( 1 ).withDayOfYear( 1 );
      org.joda.time.Period period = new org.joda.time.Period( begin, end, PeriodType.days() );
      if ( period.getDays() == 0 ) {
        return 0.0;
      }
      return period.getDays() / ( double ) begin.dayOfYear().getMaximumValue();
    }


    @Override
    public double calculateDays(@Nonnull LocalDate begin, @Nonnull LocalDate end ) {
      return new org.joda.time.Period( begin, end, PeriodType.days() ).getDays();
    }

  };


  /**
   * A time system where each month has 30 days and each year 360 days.
   */
  @Nonnull
  TimeSystem BANK_30_360 = new EqualMonthYearTimeSystem() {
    public static final int DAYS_PER_YEAR = 360;
    public static final int MAX_DAYS_PER_MONTH = 30;

    @Override
    public double getDaysPerMonth() {
      return MAX_DAYS_PER_MONTH;
    }

    @Override
    public int getDaysPerYear() {
      return DAYS_PER_YEAR;
    }
  };
  /**
   * A time system where each month has 30.416 and each year 365 (exactly 364.992) days.
   * This time system is used for PAngV 2000 calculations
   */
  @Nonnull
  TimeSystem BANK_304_365 = new EqualMonthYearTimeSystem() {
    public static final int DAYS_PER_YEAR = 365;
    public static final double MAX_DAYS_PER_MONTH = 30.416;

    @Override
    public double getDaysPerMonth() {
      return MAX_DAYS_PER_MONTH;
    }

    @Override
    public int getDaysPerYear() {
      return DAYS_PER_YEAR;
    }
  };

  @SuppressWarnings( {"PublicStaticCollectionField"} )
  @Nonnull
  List<? extends TimeSystem> AVAILABLE = Collections.unmodifiableList( Arrays.asList( TimeSystem.CALENDAR_BASED, TimeSystem.BANK_30_360, TimeSystem.BANK_304_365 ) );

  /**
   * Calculates how many days there are between the beginning and the end of the given dates
   *
   * @param begin the beginning
   * @param end   the end
   * @return the amount of days between the two dates
   */
  double calculateDays( @Nonnull LocalDate begin, @Nonnull LocalDate end );

  /**
   * Calculates the amount of years between the given dates
   *
   * @param begin the begin
   * @param end   the end
   * @return the amount of years
   */
  int calculateFullYears( @Nonnull LocalDate begin, @Nonnull LocalDate end );

  /**
   * Returns one period describing the difference between the two dates.
   *
   * @param begin the begin
   * @param end   the end
   * @return the period describing the difference
   */
  @Nonnull
  Period calculatePeriod( @Nonnull LocalDate begin, @Nonnull LocalDate end );

  /**
   * Calculates the amount of years (exactly) between the two dates
   *
   * @param begin the begin
   * @param end   the end
   * @return the exact amount of years between the two dates
   */
  double calculateYears( @Nonnull LocalDate begin, @Nonnull LocalDate end );


  /**
   * Holds informations about a period
   */
  final class Period {
    private final int years;
    private final int months;
    private final double days;

    /**
     * Creates a new period
     *
     * @param years  the amount of years
     * @param months the amount of months
     * @param days   the amount of days
     */
    public Period( int years, int months, double days ) {
      this.years = years;
      this.months = months;
      this.days = days;
    }

    public int getYears() {
      return years;
    }

    public int getMonths() {
      return months;
    }

    public double getDays() {
      return days;
    }
  }
}