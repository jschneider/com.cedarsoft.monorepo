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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Simple money implementation that is backed up by a long.
 */
public final class Money extends AbstractMoney {
  @Nonnull

  public static final String PROPERTY_NEGATIVE = "negative";

  /**
   * Represents a money with the value of 0
   */
  @Nonnull
  public static final Money ZERO = new Money();

  /**
   * A very big value for money. Can be used as upper border
   */
  @Nonnull
  public static final Money BIG = Money.getMoneyForValue( Long.MAX_VALUE );
  @Nonnull
  public static final Money SMALL = Money.getMoneyForValue( Long.MIN_VALUE );

  /**
   * This method expects internal value.
   * This depends on *{@link #SCALE}
   *
   * @param value the value of value
   * @return the money
   */
  @Nonnull
  public static Money getMoneyForValue( long value ) {
    return new Money( value );
  }

  /**
   * Internal representation:   Always an integer, scaled so that unity
   * is the smallest measurable quantity
   */
  protected final long value;//  Amount in monetary units * SCALE

  /**
   * @param amount the amount
   */
  public Money( final double amount ) {
    //    value = Math.round( amount * SCALE );
    value = ( long ) ( amount * SCALE );
  }

  public Money( @Nonnull BigDecimal bigDecimal ) {
    this( bigDecimal.multiply( new BigDecimal( SCALE ) ).longValue() );
  }

  /**
   * Creates a new money instance with the given amount as cents
   *
   * @param value the *CENTS*
   */
  private Money( final long value ) {
    this.value = value;
  }

  /**
   * Creates a new money object with an amount of "0"
   */
  public Money() {
    value = 0;
  }

  /**
   * Copy constructor
   *
   * @param value the money object the value is copied from
   */
  public Money( @Nonnull final AbstractMoney value ) {
    this.value = value.getValue();
  }

  /**
   * Returns the value. The value depends on the scale!
   * This method is for internal usage only.
   *
   * @return the value (depending on the scale)
   */
  @Override
  public long getValue() {
    return value;
  }

  /**
   * Returns a money object with the absolute value
   *
   * @return a new money object with the absolute value
   */
  @Override
  @Nonnull
  public Money abs() {
    if ( getValue() < 0 ) {
      return Money.getMoneyForValue( -value );
    } else {
      return this;
    }
  }

  /**
   * Adds the given amount
   *
   * @param money the amount that is added
   * @return the sum
   */
  @Override
  @Nonnull
  public Money plus( @Nonnull AbstractMoney money ) {
    return Money.getMoneyForValue( value + money.getValue() );
  }

  /**
   * Adds the given amount
   *
   * @param money the money that is added
   * @return the sum
   */

  @Override
  @Nonnull
  public Money plus( double money ) {
    return Money.getMoneyForValue( ( long ) ( value + money * SCALE ) );
  }

  /**
   * Multiplies with the given multiplicator
   *
   * @param multiplicator the multiplicator
   * @return the new amount
   */

  @Override
  @Nonnull
  public Money multiply( double multiplicator ) {
    return Money.getMoneyForValue( ( long ) ( value * multiplicator ) );
  }

  @Override
  @Nonnull
  public Money divide( double divisor ) {
    return Money.getMoneyForValue( ( long ) ( value / divisor ) );
  }

  @Override
  @Nonnull
  public Money minus( double money ) {
    return Money.getMoneyForValue( ( long ) ( value - money * SCALE ) );
  }

  @Override
  @Nonnull
  public Money minus( @Nonnull AbstractMoney money ) {
    return Money.getMoneyForValue( value - money.getValue() );
  }

  /**
   * Returns the negative amount
   *
   * @return the negative amount
   */
  @Override
  @Nonnull
  public Money negative() {
    return Money.getMoneyForValue( -value );
  }

  @Nonnull
  public Money getNegative() {
    return negative();
  }

  /**
   * Returns a nice representation
   *
   * @return the nice representation
   */
  @Nonnull

  public String getRepresentation() {
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMaximumFractionDigits( 2 );
    format.setMinimumFractionDigits( 2 );
    return format.format( getEstimatedValue() ) + ' ' + Currency.getInstance( Locale.getDefault() ).getSymbol( Locale.getDefault() );
  }

  @Override
  public String toString() {
    return getRepresentation();
  }

  /**
   * Returns whether the amount is greater than 0
   *
   * @return whether the amount is greater than 0
   */
  public boolean isPositive() {
    return value > 0;
  }
}
