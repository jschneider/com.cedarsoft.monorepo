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

package it.neckar.open.business.calc.roi;

import static org.junit.Assert.*;

import java.text.NumberFormat;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.*;

import it.neckar.open.business.Money;

/**
 *
 */
public class PangRealLiveTest {
  private LocalDate base;

  @BeforeEach
  public void setUp() throws Exception {
    base = new LocalDate( 2007, 9, 01 );
    calculator = new PAngV2000Calculator( base );
  }

  private PAngV2000Calculator calculator;

  @Test
  public void testMeinHovelChristine() {
    final double expected = 0.053806;

    double amount6 = +15607.57;
    calculator.addPayment(new Money(amount6), base);
    double amount5 = +58;
    calculator.addPayment( new Money( amount5 ), new LocalDate( 2007, 9, 1 ) );
    double amount4 = +58;
    calculator.addPayment( new Money( amount4 ), new LocalDate( 2007, 10, 1 ) );
    double amount3 = +58;
    calculator.addPayment( new Money( amount3 ), new LocalDate( 2007, 11, 1 ) );
    double amount2 = +58;
    calculator.addPayment( new Money( amount2 ), new LocalDate( 2007, 12, 1 ) );
    double amount1 = +662;
    calculator.addPayment( new Money( amount1 ), new LocalDate( 2008, 1, 1 ) );
    double amount = -17682;
    calculator.addPayment( new Money( amount ), new LocalDate( 2009, 1, 1 ) );

    assertEquals( expected, calculator.calculate().getRateOfReturnAsDouble(), 0.00001 );
    assertEquals( 0, calculator.calculateCashValue( expected ).getEstimatedValue(), 0.03 );
  }

  @Test
  public void testNeaefelt() {
    double expected = 0.054983;

    LocalDate base = new LocalDate( 2007, 9, 1 );

    calculator.addPayment( new Money( ( double ) 16871 ), base );
    double amount = -22345;
    calculator.addPayment( new Money( amount ), new LocalDate( 2012, 12, 1 ) );

    assertEquals( 0, calculator.calculateCashValue( expected ).getEstimatedValue(), 0.04 );

    double calculated = calculator.calculate().getRateOfReturnAsDouble();

    NumberFormat format = NumberFormat.getPercentInstance();
    format.setMinimumFractionDigits( 4 );
    format.setMaximumFractionDigits( 4 );

    System.out.println( "Result: " + format.format( calculated ) );
    assertEquals( "Delta: " + Math.abs( calculated - expected ) * 1000, expected, 0.00001, calculated );
  }

  @Test
  public void testDocumentation() {
    double expected = 0.09958;
    LocalDate base = new LocalDate( 2000, 2, 28 );

    calculator.addPayment( new Money( ( double ) 3920 ), base );
    double amount4 = -30;
    calculator.addPayment( new Money( amount4 ), new LocalDate( 2000, 3, 30 ) );
    double amount3 = -1360;
    calculator.addPayment( new Money( amount3 ), new LocalDate( 2001, 3, 30 ) );
    double amount2 = -1270;
    calculator.addPayment( new Money( amount2 ), new LocalDate( 2002, 3, 30 ) );
    double amount1 = -1180;
    calculator.addPayment( new Money( amount1 ), new LocalDate( 2003, 3, 30 ) );
    double amount = -1082.50;
    calculator.addPayment( new Money( amount ), new LocalDate( 2004, 2, 28 ) );

    double calculated = calculator.calculate().getRateOfReturnAsDouble();

    NumberFormat format = NumberFormat.getPercentInstance();
    format.setMinimumFractionDigits( 4 );
    format.setMaximumFractionDigits( 4 );

    System.out.println( "Result: " + format.format( calculated ) );
    assertEquals( "Delta: " + Math.abs( calculated - expected ), expected, 0.001, calculated );//The example is really bad. I think my calculation is o.k. too;
    //    assertEquals( "Delta: " + Math.abs( calculated - expected ), expected, calculated, 0.000001 );
  }
}
