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

package com.cedarsoft.business;

import javax.annotation.Nonnull;
import org.junit.*;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 *
 */
public class MoneyTest {
  @Test
  public void testMax() {
    assertTrue( String.valueOf( Money.BIG.getValue() ), Money.BIG.getValue() > 0 );
    assertEquals( Long.MAX_VALUE, Money.BIG.getValue() );
  }

  @Test
  public void testDiv() {
    assertEquals( 1.0, new Money( 100 ).div( new Money( 100 ) ), 0 );
    assertEquals( 0.5, new Money( 100 ).div( new Money( 200 ) ), 0 );
    assertEquals( 2.0, new Money( 400 ).div( new Money( 200 ) ), 0 );
    assertEquals( 100.0, new Money( 100 ).div( new Money( 1 ) ), 0 );
  }

  @Test
  public void testGetUnits() {
    //attention --> if the scale changes everything "explodes"
    Money money = Money.getMoneyForValue( ( long ) AbstractMoney.SCALE * 10 - 5 );
    assertEquals( AbstractMoney.SCALE * 10 - 5, money.getValue() );
    assertEquals( 10.0, money.getEstimatedValue(), 0 );
    assertEquals( 1000, money.getValueAsCents() );
    assertEquals( 0, money.getCents() );
    assertEquals( 10, money.getWholeUnits() );
  }

  @Test
  public void testCalc2() {
    assertEquals( 2003.75, new Money( 1003.75 ).plus( new Money( 1000 ) ).getEstimatedValue(), 0 );
    assertEquals( 1001.88, new Money( 2003.75 ).divide( 2 ).getEstimatedValue(), 0 );
    assertEquals( 1001.88, new Money( 1003.75 ).plus( new Money( 1000 ) ).divide( 2 ).getEstimatedValue(), 0 );
  }

  @Test
  public void testDoubleConstructor() {
    assertEquals( 1.0, new Money( 1.0 ).getEstimatedValue(), 0 );
    assertEquals( 1.0, new Money( 1.00 ).getEstimatedValue(), 0 );
    assertEquals( 1.01, new Money( 1.01 ).getEstimatedValue(), 0 );
    assertEquals( 1.01, new Money( 1.0149999999 ).getEstimatedValue(), 0 );
    assertEquals( 1.02, new Money( 1.015001 ).getEstimatedValue(), 0 );

    assertEquals( 100, new Money( 1.0 ).getValueAsCents() );
    assertEquals( 100, new Money( 1.00 ).getValueAsCents() );
    assertEquals( 101, new Money( 1.01 ).getValueAsCents() );
    assertEquals( 101, new Money( 1.0149999999 ).getValueAsCents() );
    assertEquals( 102, new Money( 1.015001 ).getValueAsCents() );
  }

  @Test
  public void testEstimatedValue() {
    assertEquals( 0.01, new Money( 0.01 ).getEstimatedValue(), 0.0 );
    assertEquals( 0.00, new Money( 0.00 ).getEstimatedValue(), 0.0 );
    assertEquals( 100000000.00, new Money( 100000000.00 ).getEstimatedValue(), 0.0 );
  }

  @Test
  public void testDecimal() {
    BigDecimal decimal = new BigDecimal( "0.01" );
    assertEquals( 0.01, decimal.doubleValue(), 0 );
    assertEquals( 1.0, decimal.multiply( new BigDecimal( "100" ) ).doubleValue(), 0 );

    assertEquals( new BigDecimal( "0.01" ), new Money( 0.01 ).getDecimalValue() );
    assertEquals( new BigDecimal( "0.5" ), new Money( 0.5 ).getDecimalValue() );
    assertEquals( new BigDecimal( "0" ), new Money( 0.00 ).getDecimalValue() );
    assertEquals( new BigDecimal( "0.001" ), new Money( 0.001 ).getDecimalValue() );
    assertEquals( new BigDecimal( "-0.001" ), new Money( -0.001 ).getDecimalValue() );

    assertEquals( new BigDecimal( "-0.001" ), new Money( new BigDecimal( "-0.001" ) ).getDecimalValue() );
  }

  @Test
  public void testEquals() {
    assertEquals( new Money( 0.01 ), new Money( 0.01 ) );
    assertEquals( new Money( 0.01 ).multiply( 2 ), new Money( 0.02 ) );
  }

  @Test
  public void testGetUnit() {
    assertEquals( 0, new Money( 0.99 ).getWholeUnits() );
    assertEquals( 99, new Money( 0.99 ).getCents() );
    assertEquals( 1, new Money( 1.01 ).getWholeUnits() );
    assertEquals( 1, new Money( 1.01 ).getCents() );
    assertEquals( 1, new Money( 1.0 ).getWholeUnits() );
    assertEquals( 0, new Money( 1.0 ).getCents() );
  }

  @Test
  public void testAdd() {
    checkMultiSmallSum( new Money( 0 ) );
    checkMultiSmallSum( new MutableMoney( 0 ) );
  }

  private static void checkMultiSmallSum( @Nonnull AbstractMoney sum ) {
    int max = 10000000;
    for ( int i = 0; i < max; i++ ) {
      sum = doIteration( sum, i );
    }
    assertEquals( max / 100.0, sum.getWholeUnits(), 0.00000001 );
  }

  private static AbstractMoney doIteration( @Nonnull AbstractMoney sum, int i ) {
    sum = sum.plus( 0.01 );

    double delta = sum.getValueAsCents() - i - 1;
    if ( delta > 0.001 ) {
      fail( "Found delta " + delta + " at i=" + i + " with value: " + sum.getValue() );
    }
    return sum;
  }

  @Test
  public void testCalc() {
    MoneyProvider mutableProvider = new MoneyProvider() {
      @Override
      @Nonnull
      public AbstractMoney getMoney() {
        return new MutableMoney( 1000 );
      }
    };
    MoneyProvider immutableProvider = new MoneyProvider() {
      @Override
      @Nonnull
      public Money getMoney() {
        return new Money( 1000 );
      }
    };

    checkCalc( mutableProvider );
    checkCalc( immutableProvider );

    checkCompare( mutableProvider );
    checkCompare( immutableProvider );
  }

  private static void checkCompare( @Nonnull MoneyProvider provider ) {
    assertEquals( 1000, provider.getMoney().getWholeUnits() );

    assertTrue( provider.getMoney().isGreaterThan( new MutableMoney( 999.99 ) ) );
    assertTrue( provider.getMoney().isGreaterThan( new Money( 999.99 ) ) );
    assertTrue( provider.getMoney().isGreaterThan( 999.99 ) );

    assertTrue( provider.getMoney().isLessThan( new MutableMoney( 1000.01 ) ) );
    assertTrue( provider.getMoney().isLessThan( new Money( 1000.01 ) ) );
    assertTrue( provider.getMoney().isLessThan( 1000.01 ) );
  }

  private static void checkCalc( @Nonnull MoneyProvider provider ) {
    assertEquals( 1000, provider.getMoney().getWholeUnits() );
    //Add
    assertEquals( 1500, provider.getMoney().plus( 500 ).getWholeUnits() );
    assertEquals( 1001, provider.getMoney().plus( 1 ).getWholeUnits() );
    assertEquals( 1000, provider.getMoney().plus( 0 ).getWholeUnits() );
    assertEquals( 1500, provider.getMoney().plus( new Money( 500 ) ).getWholeUnits() );

    //Minus
    assertEquals( 1000, provider.getMoney().minus( 0 ).getWholeUnits() );
    assertEquals( 999, provider.getMoney().minus( 1 ).getWholeUnits() );
    assertEquals( 900, provider.getMoney().minus( 100 ).getWholeUnits() );
    assertEquals( 600, provider.getMoney().minus( new Money( 400 ) ).getWholeUnits() );

    //Multiply
    assertEquals( 2000, provider.getMoney().multiply( 2 ).getWholeUnits() );
    assertEquals( 500, provider.getMoney().multiply( 0.5 ).getWholeUnits() );
    assertEquals( 1000, provider.getMoney().multiply( 1 ).getWholeUnits() );
    assertEquals( 0, provider.getMoney().multiply( 0 ).getWholeUnits() );

    //Divide
    assertEquals( 500, provider.getMoney().divide( 2 ).getWholeUnits() );
    assertEquals( 1000, provider.getMoney().divide( 1 ).getWholeUnits() );
    assertEquals( 2000, provider.getMoney().divide( 0.5 ).getWholeUnits() );
  }

  private interface MoneyProvider {
    @Nonnull
    AbstractMoney getMoney();
  }

}
