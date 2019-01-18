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

package com.cedarsoft.business.calc.roi;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;

import com.cedarsoft.business.Money;
import com.cedarsoft.business.payment.DefaultPayment;

/**
 *
 */
public class PangVCalculatorTest {
  private static final double EXPECTED_RATE = 0.12924;
  private PAngV2000Calculator calculator;
  private LocalDate base;

  @BeforeEach
  public void setUp() throws Exception {
    base = new LocalDate( 2007, 1, 1 );
    calculator = new PAngV2000Calculator( base );
    calculator.addPayment( new Money( 1000 ), base );
    calculator.addPayment( new Money( -1200 ), base.plusYears( 1 ).plusMonths( 6 ) );
  }

  @Test
  public void testImpossible() {
    calculator = new PAngV2000Calculator( base );
    calculator.addPayment( new Money( -1000 ), base.plusYears( 1 ) );
    calculator.addPayment( new Money( -1000 ), base.plusYears( 2 ) );
    assertTrue( Double.isNaN( calculator.calculate().getRateOfReturnAsDouble() ) );
  }

  @Test
  public void testNegative() {
    calculator = new PAngV2000Calculator( base );
    calculator.addPayment( new Money( -1000 ), base );
    calculator.addPayment( new Money( 1200 ), base.plusYears( 1 ).plusMonths( 6 ) );
    assertEquals( EXPECTED_RATE, calculator.calculate().getRateOfReturnAsDouble(), 0.0001 );
  }

  @Test
  public void testSimpleTest() {
    assertEquals( EXPECTED_RATE, calculator.calculate().getRateOfReturnAsDouble(), 0.0001 );
  }

  @Test
  public void testCashValue() {
    assertEquals( -200.0, calculator.calculateCashValue( 0 ).getEstimatedValue(), 0 );
    assertEquals( -40.14, calculator.calculateCashValue( 0.1 ).getEstimatedValue(), 0.001 );
    assertEquals( 0.0, calculator.calculateCashValue( EXPECTED_RATE ).getEstimatedValue(), 0.01 );
  }

  @Test
  public void testValue() {
    //A rate of null --> always the default value
    assertEquals( 1000.0, PAngV2000Calculator.calulateCashValue( base, new DefaultPayment( new Money( 1000 ), base ), 0 ).getEstimatedValue(), 0 );

    assertEquals( 1000.0, PAngV2000Calculator.calulateCashValue( base, new DefaultPayment( new Money( 1000 ), base.plusYears( 1 ) ), 0 ).getEstimatedValue(), 0 );
    assertEquals( 1000.0, PAngV2000Calculator.calulateCashValue( base, new DefaultPayment( new Money( 1000 ), base.plusYears( 2 ) ), 0 ).getEstimatedValue(), 0 );

    assertEquals( 1000.0, PAngV2000Calculator.calulateCashValue( base, new DefaultPayment( new Money( 1000 ), base ), 0.1 ).getEstimatedValue(), 0 );
    assertEquals( 909.09, PAngV2000Calculator.calulateCashValue( base, new DefaultPayment( new Money( 1000 ), base.plusYears( 1 ) ), 0.1 ).getEstimatedValue(), 0.0001 );
    assertEquals( 866.78, PAngV2000Calculator.calulateCashValue( base, new DefaultPayment( new Money( 1000 ), base.plusYears( 1 ).plusMonths( 6 ) ), 0.1 ).getEstimatedValue(), 0.0001 );
  }
}
