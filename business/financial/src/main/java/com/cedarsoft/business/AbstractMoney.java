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
import java.text.DecimalFormatSymbols;

/**
 * Abstract base class for money implementations
 */
public abstract class AbstractMoney implements Comparable<AbstractMoney> {
  public static final short SCALE = ( short ) 10000;//  Smallest fraction of monetary unit to

  /**
   * Returns the amount of whole units
   *
   * @return the amount of whole units
   */
  public long getWholeUnits() {
    return getValueAsCents() / 100;
  }

  /**
   * Returns the getCents.
   *
   * @return the getCents
   */
  public short getCents() {
    return ( short ) ( getValueAsCents() - getWholeUnits() * 100 );
  }

  /**
   * Returns the value as cents
   *
   * @return the cents
   */
  public long getValueAsCents() {
    return Math.round( getValue() / ( SCALE / 100.0 ) );
  }

  public boolean equals( @Nonnull final AbstractMoney money ) {
    return getValue() == money.getValue();
  }

  /**
   * Whether this is lesser than the given money
   *
   * @param money the other money object
   * @return whether this is lesser than the given money
   */
  public boolean isLessThan( @Nonnull final AbstractMoney money ) {
    return getValue() < money.getValue();
  }

  /**
   * Whether this is greater than the given money
   *
   * @param money the oth ermoney object
   * @return whether this is greater than the given money
   */
  public boolean isGreaterThan( @Nonnull final AbstractMoney money ) {
    return getValue() > money.getValue();
  }

  public boolean equals( final double money ) {
    return this.getValue() == ( long ) ( money * SCALE );
  }

  /**
   * Whether this is lesser than the given amount
   *
   * @param money the other money object
   * @return whether this is lesser than the given amount
   */
  public boolean isLessThan( final double money ) {
    return getValue() < ( long ) ( money * SCALE );
  }

  public boolean isGreaterThan( final double money ) {
    return getValue() > ( long ) ( money * SCALE );
  }

  public double div( final AbstractMoney money ) {
    //noinspection IntegerDivisionInFloatingPointContext
    return ( double ) getValue() / ( double ) money.getValue();
  }

  /**
   * Returns the estimated value (as double)
   *
   * @return the estimated value
   */
  public double getEstimatedValue() {
    return getValueAsCents() / 100.0;
  }

  /**
   * Returns the exact value as decimal
   *
   * @return the exact value
   */
  @Nonnull
  public BigDecimal getDecimalValue() {
    return new BigDecimal( getValue() ).divide( new BigDecimal( SCALE ) );
  }

  private static short sign( @Nonnull AbstractMoney money ) {
    long value = money.getValue();
    return ( short ) ( value > 0 ? 1 : value < 0 ? -1 : 0 );
  }

  public int compareTo( @Nonnull AbstractMoney o ) {
    return Long.valueOf( getValue() ).compareTo( o.getValue() );
  }

  @Override
  public int hashCode() {
    return Long.valueOf( getValue() ).hashCode();
  }

  @Override
  public boolean equals( final Object obj ) {
    return obj instanceof AbstractMoney && ( ( AbstractMoney ) obj ).getValue() == getValue();
  }

  @Override
  public String toString() {
    return String.valueOf( getWholeUnits() ) + DecimalFormatSymbols.getInstance().getMonetaryDecimalSeparator() + getCents();
  }

  public abstract long getValue();

  @Nonnull
  public abstract AbstractMoney abs();

  @Nonnull
  public abstract AbstractMoney plus( @Nonnull AbstractMoney money );

  @Nonnull
  public abstract AbstractMoney plus( double money );

  @Nonnull
  public abstract AbstractMoney multiply( double multiplicator );

  @Nonnull
  public abstract AbstractMoney divide( double divisor );

  @Nonnull
  public abstract AbstractMoney minus( double money );

  @Nonnull
  public abstract AbstractMoney minus( @Nonnull AbstractMoney money );

  @Nonnull
  public abstract AbstractMoney negative();
}