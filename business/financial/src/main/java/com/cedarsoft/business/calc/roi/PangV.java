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

import com.cedarsoft.business.Money;

import javax.annotation.Nonnull;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Contains the result of a PangV calculation
 */
public class PangV {

  public static final String PROPERTY_RATE_OF_RETURN = "rateOfReturn";

  /**
   * Constant that can be used if a PangV is not available
   */
  @Nonnull
  public static final PangV NULL = new PangV( 0, Money.BIG, -1, Money.ZERO ) {
    @Override
    public boolean isValidResult() {
      return false;
    }
  };


  private final double rateOfReturn;
  @Nonnull
  private final Money delta;
  private final int roundCount;
  @Nonnull
  private final Money sum;

  /**
   * Creates a new PangV.
   * If the delta is too big, this class returns
   *
   * @param rateOfReturn the rate of return
   * @param delta        the delta
   * @param roundCount   the round count
   */
  public PangV( double rateOfReturn, @Nonnull Money delta, int roundCount, @Nonnull Money sum ) {
    this.rateOfReturn = rateOfReturn;
    this.delta = delta;
    this.roundCount = roundCount;
    this.sum = sum;
  }


  /**
   * Returns the delta
   *
   * @return the delta
   */
  @Nonnull
  public Money getDelta() {
    return delta;
  }

  /**
   * Returns the round count
   *
   * @return the round count
   */
  public int getRoundCount() {
    return roundCount;
  }

  /**
   * Returns the rate of return - even if the delta is too big
   *
   * @return the rate of return
   */
  public double getRateOfReturnInternal() {
    return rateOfReturn;
  }

  /**
   * Returns the rate of return. If the delta is too big, this method will return {@link Double#NaN}
   *
   * @return the rate of return (or {@link Double#NaN} if no valid rate has been calculated
   */
  public double getRateOfReturnAsDouble() {
    if ( isValidResult() ) {
      return rateOfReturn;
    } else {
      return Double.NaN;
    }
  }

  /**
   * Whether this is a valid result
   *
   * @return whether this is a valid result
   */
  public boolean isValidResult() {
    double percentageOfDelta = delta.abs().getEstimatedValue() / sum.abs().getEstimatedValue();
    return percentageOfDelta < 0.001;
  }

  @Nonnull
  public BigDecimal getRateOfReturn() {
    if ( isValidResult() ) {
      NumberFormat format = NumberFormat.getNumberInstance( Locale.US );
      format.setMaximumFractionDigits( 4 );
      format.setMinimumFractionDigits( 4 );
      return new BigDecimal( format.format( rateOfReturn ) );
    } else {
      return new BigDecimal( "-1" );
    }
  }
}
