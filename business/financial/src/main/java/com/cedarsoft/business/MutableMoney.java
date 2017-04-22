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

/**
 * Mutable money that can be changed.
 */
public class MutableMoney extends AbstractMoney {
  private long value;

  public MutableMoney() {
  }

  public MutableMoney( @Nonnull Money money ) {
    this.value = money.getValue();
  }

  public MutableMoney( final double amount ) {
    value = Math.round( amount * SCALE );
  }

  /**
   * Sets the value of the given money.
   *
   * @param money the money object the value is taken from
   * @return this
   */
  public MutableMoney setValue( @Nonnull final Money money ) {
    this.value = money.value;
    return this;
  }

  /**
   * Sets the value
   *
   * @param money the money
   * @return this
   */
  public MutableMoney setValue( final double money ) {
    this.value = Math.round( money * SCALE );
    return this;
  }

  public void setValue( long value ) {
    this.value = value;
  }

  @Override
  public long getValue() {
    return value;
  }

  @Override
  @Nonnull
  public MutableMoney plus( @Nonnull AbstractMoney money ) {
    value += money.getValue();
    return this;
  }

  @Override
  @Nonnull
  public MutableMoney plus( double money ) {
    value = ( long ) ( money * SCALE + value );
    return this;
  }

  @Override
  @Nonnull
  public MutableMoney multiply( double multiplicator ) {
    value = ( long ) ( multiplicator * value );
    return this;
  }

  @Override
  @Nonnull
  public MutableMoney divide( double divisor ) {
    value = ( long ) ( ( double ) value / divisor );
    return this;
  }

  @Override
  @Nonnull
  public MutableMoney minus( double money ) {
    value -= ( long ) ( money * SCALE );
    return this;
  }

  @Override
  @Nonnull
  public MutableMoney minus( @Nonnull AbstractMoney money ) {
    value -= money.getValue();
    return this;
  }

  @Override
  @Nonnull
  public MutableMoney abs() {
    if ( value < 0 ) {
      value = -value;
    }
    return this;
  }

  @Override
  @Nonnull
  public MutableMoney negative() {
    value = -value;
    return this;
  }

  /**
   * Returns an immutable money
   *
   * @return the immutable money
   */
  @Nonnull
  public Money getMoney() {
    return Money.getMoneyForValue( getValue() );
  }

  public Money immutable() {
    return getMoney();
  }
}
