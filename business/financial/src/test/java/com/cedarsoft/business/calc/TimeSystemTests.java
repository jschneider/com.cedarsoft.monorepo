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

import static org.junit.Assert.*;

import javax.annotation.Nonnull;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.jupiter.api.*;

/**
 *
 */
public class TimeSystemTests {
  @Test
  public void testSums() {
    runSumTest( TimeSystem.BANK_30_360 );
    runSumTest( TimeSystem.BANK_304_365 );
  }

  @Test
  public void testSumCalendarBased() {
    runSumTest( TimeSystem.CALENDAR_BASED );
  }

  private void runSumTest( @Nonnull TimeSystem timeSystem ) {
    LocalDate start = new LocalDate( 2007, 1, 1 );
    LocalDate end = start.plusYears( 1 );

    double daySum = 0;
    double yearSum = 0;

    LocalDate runner = start;
    while ( runner.isBefore( end ) ) {
      double days = timeSystem.calculateDays( runner, runner.plusMonths( 1 ) );
      assertTrue( "uups - too many days " + days, days < 31.1 );
      daySum += days;
      double years = timeSystem.calculateYears( runner, runner.plusMonths( 1 ) );
      assertTrue( "uups - too many years " + years, years < 0.15 );
      yearSum += years;
      runner = runner.plusMonths( 1 );
    }

    assertEquals( 1.0, yearSum, 0.00000000001 );
    assertTrue( "Wrong daySum: " + daySum, 364.992 == daySum || 365.0 == daySum || 360.0 == daySum );
  }

  @Test
  public void testCalendarDays() {
    LocalDate start = new LocalDate( 2006, 1, 1 );
    for ( int i = 0; i < 10000; i++ ) {
      LocalDate end = start.plusDays( i );
      Period period = new Period( start, end, PeriodType.days() );
      assertEquals( i, period.getDays() );

      assertEquals( i, TimeSystem.CALENDAR_BASED.calculateDays( start, end ), 0 );
    }
  }

  @Test
  public void testBugSearch() {
    Period period = new Period( new LocalDate( 2006, 1, 1 ), new LocalDate( 2010, 1, 1 ), PeriodType.days() );
    assertEquals( 1461, period.getDays() );

    assertEquals( 1460.0, TimeSystem.CALENDAR_BASED.calculateDays( new LocalDate( 2006, 1, 1 ), new LocalDate( 2010, 1, 1 ).minusDays( 1 ) ), 0 );
    assertEquals( 1461.0, TimeSystem.CALENDAR_BASED.calculateDays( new LocalDate( 2006, 1, 1 ), new LocalDate( 2010, 1, 1 ) ), 0 );
    assertEquals( 1461.0, TimeSystem.CALENDAR_BASED.calculateDays( new LocalDate( 2006, 1, 1 ), new LocalDate( 2009, 12, 31 ).plusDays( 1 ) ), 0 );
  }

  @Test
  public void testCalendarBasedYears() {
    TimeSystem.Period period = TimeSystem.CALENDAR_BASED.calculatePeriod( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 2 ) );
    assertEquals( 0, period.getYears() );
    assertEquals( 0, period.getMonths() );
    assertEquals( 1.0, period.getDays(), 0 );

    assertEquals( 1, TimeSystem.CALENDAR_BASED.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ) );
    assertEquals( 1.0, TimeSystem.CALENDAR_BASED.calculateYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ), 0 );
    assertEquals( 1, TimeSystem.CALENDAR_BASED.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 5, 1 ) ) );
    assertEquals( 1 + ( 31 + 29 + 31 + 30 ) / 366.0, TimeSystem.CALENDAR_BASED.calculateYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 5, 1 ) ), 0 );
    assertEquals( 1, TimeSystem.CALENDAR_BASED.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 12, 31 ) ) );

    assertEquals( 1, TimeSystem.CALENDAR_BASED.calculateFullYears( new LocalDate( 2007, 1, 2 ), new LocalDate( 2009, 1, 1 ) ) );

    assertEquals( 2, TimeSystem.CALENDAR_BASED.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2009, 1, 1 ) ) );
    assertEquals( 3, TimeSystem.CALENDAR_BASED.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2010, 1, 1 ) ) );

    assertEquals( 365.0, TimeSystem.CALENDAR_BASED.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ), 0 );
  }

  @Test
  public void testAllDates() {
    LocalDate begin = new LocalDate( 2007, 2, 25 );
    LocalDate end = new LocalDate( 2007, 3, 1 );

    assertEquals( 4, new Interval( begin.toDateMidnight(), end.toDateMidnight() ).toPeriod().getDays() );

    for ( TimeSystem timeSystem : TimeSystem.AVAILABLE ) {
      assertEquals( 4.0, timeSystem.calculateDays( begin, end ), 0 );
      assertEquals( 1, timeSystem.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ) );
      assertEquals( 1.0, timeSystem.calculateYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ), 0 );

      assertEquals( 0, timeSystem.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 1 ) ) );
      assertEquals( 0.0, timeSystem.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 1 ) ), 0 );
      assertEquals( 0.0, timeSystem.calculateYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 1 ) ), 0 );
    }
  }

  @Test
  public void test30_360() {
    assertEquals( 1, TimeSystem.BANK_30_360.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ) );

    {
      TimeSystem.Period period = TimeSystem.BANK_30_360.calculatePeriod( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 31 ) );
      assertEquals( 0, period.getYears() );
      assertEquals( 11, period.getMonths() );
      assertEquals( 30.0, period.getDays(), 0 );
    }
    {
      TimeSystem.Period period = TimeSystem.BANK_30_360.calculatePeriod( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 30 ) );
      assertEquals( 0, period.getYears() );
      assertEquals( 11, period.getMonths() );
      assertEquals( 29.0, period.getDays(), 0 );
    }
    {
      TimeSystem.Period period = TimeSystem.BANK_30_360.calculatePeriod( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 29 ) );
      assertEquals( 0, period.getYears() );
      assertEquals( 11, period.getMonths() );
      assertEquals( 28.0, period.getDays(), 0 );
    }

    assertEquals( 360.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ), 0 );
    assertEquals( 360.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 31 ) ), 0 );
    assertEquals( 359.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 30 ) ), 0 );
    assertEquals( 358.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 29 ) ), 0 );
    assertEquals( 357.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 28 ) ), 0 );
    assertEquals( 356.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 27 ) ), 0 );
    assertEquals( 355.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 26 ) ), 0 );

    assertEquals( 720.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2009, 1, 1 ) ), 0 );


    //Einzelner Monat
    assertEquals( 0.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 1 ) ), 0 );
    assertEquals( 1.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 2 ) ), 0 );
    assertEquals( 2.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 3 ) ), 0 );
    assertEquals( 3.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 4 ) ), 0 );
    assertEquals( 4.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 5 ) ), 0 );
    assertEquals( 5.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 6 ) ), 0 );

    assertEquals( 26.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 27 ) ), 0 );
    assertEquals( 27.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 28 ) ), 0 );
    assertEquals( 28.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 29 ) ), 0 );
    assertEquals( 29.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 30 ) ), 0 );
    assertEquals( 30.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 31 ) ), 0 );
    assertEquals( 30.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 1 ) ), 0 );
    assertEquals( 31.0, TimeSystem.BANK_30_360.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 2 ) ), 0 );

    //Einzelner Monat als Jahr
    assertEquals( 1.0 / 12.0, TimeSystem.BANK_30_360.calculateYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 31 ) ), 0 );
    assertEquals( 1.0 / 12.0, TimeSystem.BANK_30_360.calculateYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 1 ) ), 0 );
  }

  @Test
  public void test304_365() {
    assertEquals( 1, TimeSystem.BANK_304_365.calculateFullYears( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ) );

    {
      TimeSystem.Period period = TimeSystem.BANK_304_365.calculatePeriod( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 31 ) );
      assertEquals( 0, period.getYears() );
      assertEquals( 11, period.getMonths() );
      assertEquals( 30.0, period.getDays(), 0 );
    }
    {
      TimeSystem.Period period = TimeSystem.BANK_304_365.calculatePeriod( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 30 ) );
      assertEquals( 0, period.getYears() );
      assertEquals( 11, period.getMonths() );
      assertEquals( 29.0, period.getDays(), 0 );
    }
    {
      TimeSystem.Period period = TimeSystem.BANK_304_365.calculatePeriod( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 29 ) );
      assertEquals( 0, period.getYears() );
      assertEquals( 11, period.getMonths() );
      assertEquals( 28.0, period.getDays(), 0 );
    }

    assertEquals( 365.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ), 0 );
    assertEquals( 364.576, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 31 ) ), 0 );
    assertEquals( 363.576, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 12, 30 ) ), 0 );

    assertEquals( 730.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2009, 1, 1 ) ), 0 );

    //
    assertEquals( 0.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 1 ) ), 0 );
    assertEquals( 1.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 2 ) ), 0 );
    assertEquals( 2.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 3 ) ), 0 );
    assertEquals( 3.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 4 ) ), 0 );
    assertEquals( 4.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 5 ) ), 0 );
    assertEquals( 5.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 6 ) ), 0 );

    assertEquals( 26.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 27 ) ), 0 );
    assertEquals( 27.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 28 ) ), 0 );
    assertEquals( 28.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 29 ) ), 0 );
    assertEquals( 29.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 30 ) ), 0 );
    assertEquals( 30.0, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 31 ) ), 0 );
    assertEquals( 30.416, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 1 ) ), 0 );
    assertEquals( 31.416, TimeSystem.BANK_304_365.calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 2 ) ), 0 );
  }
}
