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

import com.cedarsoft.business.Money;
import com.cedarsoft.business.MutableMoney;
import com.cedarsoft.business.payment.DefaultPayment;
import org.joda.time.LocalDate;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class InterestCalculatorTest {
  @Test
  public void testSum() {
    runSumTest( TimeSystem.BANK_30_360 );
    runSumTest( TimeSystem.BANK_304_365 );
  }

  @Test
  public void testSumCal() {
    runSumTest( TimeSystem.CALENDAR_BASED );
  }

  private void runSumTest( TimeSystem timeSystem ) {
    InterestCalculator calculator = new InterestCalculator( InterestCalculationSystem.SIMPLE, timeSystem, 0.1 );

    LocalDate start = new LocalDate( 2007, 1, 1 );
    LocalDate end = new LocalDate( 2008, 1, 1 );

    calculator.addPayment( new DefaultPayment( new Money( 100 ), start ) );
    assertEquals( 10.0, calculator.calculateInterestUntil( end ).getEstimatedValue(), 0 );

    //Now do this for each month
    LocalDate runner = start;
    MutableMoney sum = new MutableMoney();
    while ( runner.isBefore( end ) ) {
      calculator = new InterestCalculator( InterestCalculationSystem.SIMPLE, timeSystem, 0.1 );
      calculator.addPayment( new DefaultPayment( new Money( 100 ), runner ) );

      sum.plus( calculator.calculateInterestUntil( runner.plusMonths( 1 ) ) );
      runner = runner.plusMonths( 1 );
    }

    assertEquals( 10.0, sum.getEstimatedValue(), 0 );
  }

  @Test
  public void testTwoMonths() {
    InterestCalculator calculator = new InterestCalculator( InterestCalculationSystem.SIMPLE, TimeSystem.BANK_30_360, 0.12 );
    calculator.addPayment( new DefaultPayment( new Money( 100 ), new LocalDate( 2007, 11, 1 ) ) );

    assertEquals( 1.0, calculator.calculateInterestUntil( new LocalDate( 2007, 12, 1 ) ).getEstimatedValue(), 0 );

    assertEquals( 2.0, calculator.calculateInterestUntil( new LocalDate( 2007, 12, 31 ) ).getEstimatedValue(), 0 );
    assertEquals( 2.0, calculator.calculateInterestUntil( new LocalDate( 2008, 1, 1 ) ).getEstimatedValue(), 0 );
    assertEquals( 3.0, calculator.calculateInterestUntil( new LocalDate( 2008, 2, 1 ) ).getEstimatedValue(), 0 );
    assertEquals( 4.0, calculator.calculateInterestUntil( new LocalDate( 2008, 3, 1 ) ).getEstimatedValue(), 0 );
  }

  @Test
  public void testOther() {
    InterestCalculator calculator = new InterestCalculator( InterestCalculationSystem.SIMPLE, TimeSystem.BANK_30_360, 0.12 );
    calculator.addPayment( new DefaultPayment( new Money( 100 ), new LocalDate( 2007, 11, 1 ) ) );
    calculator.addPayment( new DefaultPayment( new Money( 100 ), new LocalDate( 2007, 12, 1 ) ) );

    assertEquals( 1.0, calculator.calculateInterestUntil( new LocalDate( 2007, 12, 1 ) ).getEstimatedValue(), 0 );
    assertEquals( 3.0, calculator.calculateInterestUntil( new LocalDate( 2007, 12, 31 ) ).getEstimatedValue(), 0 );
    assertEquals( 3.0, calculator.calculateInterestUntil( new LocalDate( 2008, 1, 1 ) ).getEstimatedValue(), 0 );
  }


  @Test
  public void testOtherPayments() {
    InterestCalculator calculator = new InterestCalculator( InterestCalculationSystem.SIMPLE, TimeSystem.BANK_30_360, 0.12 );
    calculator.addPayment( new DefaultPayment( new Money( 100 ), new LocalDate( 2007, 11, 1 ) ) );
    assertEquals( 1.0, calculator.calculateInterestUntil( new LocalDate( 2007, 12, 1 ) ).getEstimatedValue(), 0 );

    calculator.addPayment( new DefaultPayment( new Money( 100 ), new LocalDate( 2008, 1, 1 ) ) );
    assertEquals( 0.0, calculator.calculateInterestUntil( new LocalDate( 2007, 12, 1 ) ).getEstimatedValue(), 0 );
  }

  @Test
  public void testEinfach() {
    InterestCalculator calculator = new InterestCalculator( InterestCalculationSystem.SIMPLE, TimeSystem.CALENDAR_BASED, 0.1 );

    calculator.addPayment( new DefaultPayment( new Money( 1000 ), new LocalDate( 2007, 1, 1 ) ) );
    assertEquals( 100.0, calculator.calculateInterestUntil( new LocalDate( 2008, 1, 1 ) ).getEstimatedValue(), 0 );

    calculator.addPayment( new DefaultPayment( new Money( 2000 ), new LocalDate( 2007, 1, 1 ) ) );
    assertEquals( 300.0, calculator.calculateInterestUntil( new LocalDate( 2008, 1, 1 ) ).getEstimatedValue(), 0 );
  }

  @Test
  public void testZinsesZins() {
    InterestCalculator calculator = new InterestCalculator( InterestCalculationSystem.DAILY_COMPOUND_INTEREST, TimeSystem.CALENDAR_BASED, 0.1 );

    calculator.addPayment( new DefaultPayment( new Money( 1000 ), new LocalDate( 2007, 1, 1 ) ) );
    assertEquals( 105.16, calculator.calculateInterestUntil( new LocalDate( 2008, 1, 1 ) ).getEstimatedValue(), 0 );
  }

  @Test
  public void testIt() {
    InterestCalculator calculator = new InterestCalculator( InterestCalculationSystem.SIMPLE, TimeSystem.BANK_30_360, 0.12 );

    LocalDate start = new LocalDate( 2007, 1, 1 );
    LocalDate end = new LocalDate( 2007, 1, 31 );

    calculator.addPayment( new DefaultPayment( new Money( 1000 ), start ) );

    assertEquals( 10.0, calculator.calculateInterestUntil( end ).getEstimatedValue(), 0 );
  }

  @Test
  public void testMultiply() {
    assertEquals( 10.0, 1000 * 0.12 * ( 1.0 / 12.0 ), 0 );

    assertEquals( 100.0, 1000 * ( 10.0 / 12.0 ) * 0.12, 0 );
    assertEquals( 100.0, 1000 * 0.12 * ( 10.0 / 12.0 ), 0 );
  }
}
