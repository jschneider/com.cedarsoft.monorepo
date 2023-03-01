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

package it.neckar.open.business;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.Years;
import org.joda.time.chrono.GJChronology;
import org.junit.jupiter.api.*;

/**
 *
 */
public class JodaTest {
  private DateTime dateTime;

  @BeforeEach
  public void setUp() throws Exception {
    dateTime = new DateTime( new GregorianCalendar( 2007, Calendar.SEPTEMBER, 18, 14, 53 ) );
  }

  @Test
  public void testIt() {
    {
      DateMidnight begin = new DateMidnight( 2008, 2, 15 );
      DateMidnight end = new DateMidnight( 2008, 3, 15 );

      Period period = new Period( begin, end, PeriodType.yearMonthDay() );
      assertEquals( 1, period.getMonths() );
      assertEquals( 0, period.getDays() );
    }

    //    {
    //      DateMidnight begin = new DateMidnight( 2008, 2, 28 );
    //      DateMidnight end = new DateMidnight( 2008, 3, 30 );
    //
    //      Period period = new Period( begin, end, PeriodType.yearMonthDay() );
    //      assertEquals( 1, period.getMonths() );
    //      assertEquals( 0, period.getDays() );
    //    }
    //sent this to mailing list
  }

  @Test
  public void testPeriode2() {
    assertEquals( 1, Years.years( 1 ).toPeriod().getYears() );
    assertEquals( 0, Years.years( 1 ).toPeriod().getDays() );

    assertEquals( 31, new Interval( new DateMidnight( 2007, 8, 1 ), new DateMidnight( 2007, 9, 1 ) ).toPeriod( PeriodType.days() ).getDays() );
    assertEquals( PeriodType.standard(), new Interval( new DateMidnight( 2007, 8, 1 ), new DateMidnight( 2007, 9, 1 ) ).toPeriod().getPeriodType() );
  }

  @Test
  public void testPeriodeLikePangV() {
    {
      DateMidnight base = new DateMidnight( 2007, 2, 18 );
      DateMidnight other = new DateMidnight( 2008, 2, 18 );

      Period periode = new Period( base, other, PeriodType.yearMonthDay() );
      assertEquals( 1, periode.getYears() );
      assertEquals( 0, periode.getMonths() );
      assertEquals( 0, periode.getDays() );

      double days = periode.getYears() * 365 + periode.getMonths() * 30.416 + periode.getDays();
      assertEquals( 365.0, days, 0 );
    }
    {
      DateMidnight base = new DateMidnight( 2007, 2, 18 );
      DateMidnight other = new DateMidnight( 2008, 3, 18 );

      Period periode = new Period( base, other, PeriodType.yearMonthDay() );
      assertEquals( 1, periode.getYears() );
      assertEquals( 1, periode.getMonths() );
      assertEquals( 0, periode.getDays() );

      double days = periode.getYears() * 365 + periode.getMonths() * 30.416 + periode.getDays();
      assertEquals( 395.416, days, 0 );
    }
  }

  @Test
  public void testBasic() {
    assertNotNull( dateTime.dayOfMonth() );
    assertEquals( "dayOfMonth", dateTime.dayOfMonth().getName() );
    assertEquals( 18, dateTime.dayOfMonth().get() );

    assertEquals( 18, dateTime.getDayOfMonth() );
    assertEquals( 9, dateTime.getMonthOfYear() );

    assertEquals( GJChronology.class, dateTime.getChronology().getClass() );
  }

  @Test
  public void testPeriode() {
    Interval interval = new Interval( dateTime.dayOfMonth().roundFloorCopy(), new DateMidnight( 2007, 9, 21 ) );

    assertEquals( 3, interval.toPeriod().getDays() );
  }

  @Test
  public void testNames() {
    assertEquals( "9", dateTime.monthOfYear().getAsString() );
    assertEquals( "September", dateTime.monthOfYear().getAsText( Locale.GERMANY ) );
    assertEquals( "September", dateTime.monthOfYear().getAsText( Locale.US ) );
    assertEquals( "septembre", dateTime.monthOfYear().getAsText( Locale.FRENCH ) );
  }

  @Test
  public void testRound() {
    DateTime rounded = dateTime.dayOfMonth().roundFloorCopy();
    assertEquals( 0, rounded.getHourOfDay() );
    assertEquals( rounded, new DateMidnight( new GregorianCalendar( 2007, Calendar.SEPTEMBER, 18 ) ) );
  }

  @Test
  public void testLeapYear() {
    assertFalse( new DateTime( new GregorianCalendar( 2007, Calendar.SEPTEMBER, 18 ) ).year().isLeap() );
    assertFalse( new DateTime( new GregorianCalendar( 2100, Calendar.SEPTEMBER, 18 ) ).year().isLeap() );
    assertTrue( new DateTime( new GregorianCalendar( 2004, Calendar.SEPTEMBER, 18 ) ).year().isLeap() );
    assertTrue( new DateTime( new GregorianCalendar( 2000, Calendar.SEPTEMBER, 18 ) ).year().isLeap() );
  }

  @Test
  public void testNoLeap() {
    {
      DateTime first = new DateTime( new GregorianCalendar( 2008, Calendar.FEBRUARY, 28 ) );
      DateTime second = new DateTime( new GregorianCalendar( 2008, Calendar.MARCH, 30 ) );

      Period periode = new Interval( first, second ).toPeriod( PeriodType.yearMonthDay() );
      assertEquals( 0, periode.getYears() );
      assertEquals( 1, periode.getMonths() );
      assertEquals( 2, periode.getDays() );
    }


    {
      DateTime first = new DateTime( new GregorianCalendar( 2008, Calendar.FEBRUARY, 28 ) );
      DateTime second = new DateTime( new GregorianCalendar( 2008, Calendar.MARCH, 1 ) );

      Period periode = new Interval( first, second ).toPeriod( PeriodType.yearMonthDay() );
      assertEquals( 0, periode.getYears() );
      assertEquals( 0, periode.getMonths() );
      assertEquals( 2, periode.getDays() );
    }
  }

  @Test
  public void testDaysOnly() {
    DateMidnight date = new DateMidnight( new GregorianCalendar( 2007, Calendar.SEPTEMBER, 18 ) );
    assertEquals( date, new DateMidnight( new GregorianCalendar( 2007, Calendar.SEPTEMBER, 18 ) ) );

    assertEquals( 21, date.getCenturyOfEra() );
    DateMidnight other = date.withMonthOfYear( 8 );

    Interval interval = new Interval( other, date );
    Period period = interval.toPeriod( PeriodType.days() );
    assertEquals( 31, period.getDays() );
  }
}
