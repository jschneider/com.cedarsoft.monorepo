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
import javax.annotation.Nonnull;

import javax.annotation.Nonnull;

/**
 * Represents a special kind of rent calculation system (e.g. with or without compound interest)
 */
public interface InterestCalculationSystem {
  /**
   * A simple system that just calculates a plain interest rate.
   * <p/>
   * This system only works with the intervalInYears - the intervalInDays is ignored
   */
  @Nonnull
  InterestCalculationSystem SIMPLE = new InterestCalculationSystem() {
    @Nonnull
    public Money calculateInterest( @Nonnull Money amount, double intervalInYears, double intervalInDays, double interestRate ) {
      return Money.getMoneyForValue( ( long ) ( amount.getValue() * interestRate * intervalInYears ) );
    }

    public boolean isCompoundSystem() {
      return false;
    }
  };

  /**
   * Calculates the compound interest
   */
  @Nonnull
  InterestCalculationSystem DAILY_COMPOUND_INTEREST = new InterestCalculationSystem() {
    @Nonnull
    public Money calculateInterest( @Nonnull Money amount, double intervalInYears, double intervalInDays, double interestRate ) {
      double daysPerYear = intervalInDays / intervalInYears;
      return amount.multiply( Math.pow( 1 + interestRate / daysPerYear, intervalInDays ) ).minus( amount );
    }

    public boolean isCompoundSystem() {
      return true;
    }
  };

  /**
   * Calculates the interest
   *
   * @param amount          the amount the interest is calculated for
   * @param intervalInYears the interval in years
   * @param intervalInDays  the interval in days
   * @param interestRate    the interest rate
   * @return the interest
   */
  @Nonnull
  Money calculateInterest( @Nonnull Money amount, double intervalInYears, double intervalInDays, double interestRate );

  /**
   * Whether the system is a compound interest system
   *
   * @return true if compound interest is calculated
   */
  boolean isCompoundSystem();
}