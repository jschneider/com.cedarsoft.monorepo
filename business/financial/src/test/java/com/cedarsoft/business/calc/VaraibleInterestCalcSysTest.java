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

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;

import com.cedarsoft.business.Money;

/**
 *
 */
public class VaraibleInterestCalcSysTest {
  private VariableInterestCalculationSystem simpleSystem;
  private VariableInterestCalculationSystem compoundSystem;
  private MappedInterestRateProvider interestRateProvider;

  @BeforeEach
  public void setUp() throws Exception {
    interestRateProvider = new MappedInterestRateProvider( 0.1 );
    interestRateProvider.setRate( 0.05, new YearMonth( 2008, 1 ) );

    simpleSystem = new VariableInterestCalculationSystem( interestRateProvider, InterestCalculationSystem.SIMPLE, TimeSystem.CALENDAR_BASED );
    compoundSystem = new VariableInterestCalculationSystem( interestRateProvider, InterestCalculationSystem.DAILY_COMPOUND_INTEREST, TimeSystem.CALENDAR_BASED );
  }

  @AfterEach
  public void tearDown() throws Exception {

  }

  @Test
  public void testSetup() {
    assertNotNull( interestRateProvider );
    assertNotNull( compoundSystem );
  }

  @Test
  public void testInterestRates() {
    Rent rent = simpleSystem.calculateInterestMonthlyPeriod( new Money( 1000 ), new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 2, 1 ) );
    assertEquals( 4.23, rent.getInterest().getEstimatedValue(), 0 );

    List<? extends InterestDetails> detailsList = rent.getInterestDetails();
    assertNotNull( detailsList );
    assertEquals( 1, detailsList.size() );
    InterestDetails details = detailsList.get( 0 );
    assertEquals( 31.0, details.getDays(), 0 );
    assertEquals( 0.05, details.getRateAsDouble(), 0 );
  }

  @Test
  public void testInterestRates2() {
    Rent rent = compoundSystem.calculateInterestMonthlyPeriod( new Money( 100000 ), new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 2, 15 ) );
    assertEquals( 809.19, rent.getInterest().getEstimatedValue(), 0 );

    List<? extends InterestDetails> interestInformation = rent.getInterestDetails();
    assertNotNull( interestInformation );
    assertEquals( 2, interestInformation.size() );
    {
      InterestDetails details = interestInformation.get( 0 );
      assertEquals( 31.0, details.getDays(), 0 );
      assertEquals( 0.05, details.getRateAsDouble(), 0 );
      assertEquals( 0.0, details.getBaseValue().getEstimatedValue(), 0 );
      assertEquals( 424.37, details.getInterest().getEstimatedValue(), 0.01 );
    }
    {
      InterestDetails details = interestInformation.get( 1 );
      assertEquals( 14.0, details.getDays(), 0 );
      assertEquals( 0.1, details.getRateAsDouble(), 0 );
      assertEquals( 424.37, details.getBaseValue().getEstimatedValue(), 0 );
      assertEquals( 384.82, details.getInterest().getEstimatedValue(), 0 );
    }
  }

  @Test
  public void testConstant() {
    VariableInterestCalculationSystem system = new VariableInterestCalculationSystem( new DefaultInterestRateProvider( 0.1 ), InterestCalculationSystem.SIMPLE, TimeSystem.CALENDAR_BASED );
    assertEquals( 10.0, system.calculateInterest( new Money( 100 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ).getInterest().getEstimatedValue(), 0 );
  }

  @Test
  public void testMonth() {
    assertEquals( 100.0 * 31 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 1 ) ).getInterest().getEstimatedValue(), 0.01 );
    assertEquals( 100.0 * 32 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 2 ) ).getInterest().getEstimatedValue(), 0.01 );
  }

  @Test
  public void testBug() {
    assertEquals( 100.0 * 29 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 30 ) ).getInterest().getEstimatedValue(), 0.01 );
    assertEquals( 100.0 * 30 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 1, 31 ) ).getInterest().getEstimatedValue(), 0.01 );
    assertEquals( 100.0 * 31 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 1 ) ).getInterest().getEstimatedValue(), 0.01 );

    assertEquals( 58.0, simpleSystem.getTimeSystem().calculateDays( new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 28 ) ), 0 );

    assertEquals( 100.0 * 57 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 27 ) ).getInterest().getEstimatedValue(), 0.01 );
    assertEquals( 100.0 * 58 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 2, 28 ) ).getInterest().getEstimatedValue(), 0.01 );
    assertEquals( 100.0 * 59 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 3, 1 ) ).getInterest().getEstimatedValue(), 0.01 );
    assertEquals( 100.0 * 60 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 3, 2 ) ).getInterest().getEstimatedValue(), 0.01 );
    assertEquals( 100.0 * 61 / 365.0, simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2007, 3, 3 ) ).getInterest().getEstimatedValue(), 0.01 );
  }

  @Test
  public void testAddition() {
    double rentPerDay = 100.0 / 365.0;

    LocalDate begin = new LocalDate( 2005, 1, 1 );
    for ( int i = 0; i < 800; i++ ) {
      assertEquals( i * rentPerDay, simpleSystem.calculateInterest( new Money( 1000 ), begin, begin.plusDays( i ) ).getInterest().getEstimatedValue(), 0.01 );
    }
  }

  @Test
  public void testOneYear() {
    assertEquals( 10.0, simpleSystem.calculateInterest( new Money( 100 ), new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) ).getInterest().getEstimatedValue(), 0 );
  }

  @Test
  public void testTime() {
    assertEquals( 31.0, TimeSystem.CALENDAR_BASED.calculateDays( new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 2, 1 ) ), 0 );
    assertEquals( 31.0 / 366.0, TimeSystem.CALENDAR_BASED.calculateYears( new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 2, 1 ) ), 0 );
  }

  @Test
  public void testOneMonth() {
    assertEquals( 4.23, InterestCalculationSystem.SIMPLE.calculateInterest( new Money( 1000 ), 31.0 / 366.0, 31, 0.05 ).getEstimatedValue(), 0 );
    Money simpleValue = simpleSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 2, 1 ) ).getInterest();
    assertEquals( 4.23, simpleValue.getEstimatedValue(), 0 );
    Money compoundValue = compoundSystem.calculateInterest( new Money( 1000 ), new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 2, 1 ) ).getInterest();
    assertEquals( 4.24, compoundValue.getEstimatedValue(), 0 );

    assertTrue( simpleValue.getValue() < compoundValue.getValue() );
  }

  @Test
  public void testOneYearCompount() {
    LocalDate begin = new LocalDate( 2007, 1, 1 );
    LocalDate end = new LocalDate( 2008, 1, 1 );
    assertEquals( 365.0, TimeSystem.CALENDAR_BASED.calculateDays( begin, end ), 0 );

    assertEquals( 1.0, TimeSystem.CALENDAR_BASED.calculateYears( begin, end ), 0 );

    Money expected = InterestCalculationSystem.DAILY_COMPOUND_INTEREST.calculateInterest( new Money( 100 ), 1, 365, 0.1 );
    assertEquals( 10.52, expected.getEstimatedValue(), 0 );
    Money actual = compoundSystem.calculateInterest( new Money( 100 ), begin, end ).getInterest();
    assertEquals( String.valueOf( actual.getValue() ), 10.52, actual.getEstimatedValue(), 0.01 );
  }

  @Test
  public void testOneYearBig() {
    LocalDate begin = new LocalDate( 2007, 1, 1 );
    LocalDate end = new LocalDate( 2008, 1, 1 );
    assertEquals( 365.0, TimeSystem.CALENDAR_BASED.calculateDays( begin, end ), 0 );

    assertEquals( 1.0, TimeSystem.CALENDAR_BASED.calculateYears( begin, end ), 0 );

    assertEquals( 1051557.82, InterestCalculationSystem.DAILY_COMPOUND_INTEREST.calculateInterest( new Money( 10000000 ), 1, 365, 0.1 ).getEstimatedValue(), 0 );
    Money actual = compoundSystem.calculateInterest( new Money( 10000000 ), begin, end ).getInterest();
    assertEquals( 1051557.82, actual.getEstimatedValue(), 0.01005 );
  }

  @Test
  public void testChangingMonths() {
    assertEquals( 121.58, simpleSystem.calculateInterest( new Money( 10000 ), new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 3, 1 ) ).getInterest().getEstimatedValue(), 0 );
    assertEquals( 122.31, compoundSystem.calculateInterest( new Money( 10000 ), new LocalDate( 2008, 1, 1 ), new LocalDate( 2008, 3, 1 ) ).getInterest().getEstimatedValue(), 0 );
  }
}
