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
import javax.annotation.Nonnull;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Calculates the interests for a given {@link InterestRateProvider}.
 */
public class VariableInterestCalculationSystem {
  @Nonnull
  private final InterestRateProvider interestRateProvider;
  @Nonnull
  private final InterestCalculationSystem backingCalculationSystem;
  @Nonnull
  private final TimeSystem timeSystem;

  /**
   * Creates a new calculation system
   *
   * @param interestRateProvider     the interest rate provider
   * @param backingCalculationSystem the backing calculation system
   * @param timeSystem               the time system
   */
  public VariableInterestCalculationSystem( @Nonnull InterestRateProvider interestRateProvider, @Nonnull InterestCalculationSystem backingCalculationSystem, @Nonnull TimeSystem timeSystem ) {
    this.interestRateProvider = interestRateProvider;
    this.backingCalculationSystem = backingCalculationSystem;
    this.timeSystem = timeSystem;
  }

  /**
   * Calculates the interest for the given amount and dates.
   *
   * @param amount the amount the interest is calculated for
   * @param begin  the begin of the period
   * @param end    the end of the period
   * @return the payment rent
   */
  @Nonnull
  public Rent calculateInterest( @Nonnull Money amount, @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    //Switch over the validity period
    switch ( getInterestRateProvider().getValidityPeriod() ) {
      case CONSTANT:
        return calculateInterestConstantRate( amount, begin, end );
      case MONTHLY:
        return calculateInterestMonthlyPeriod( amount, begin, end );
      default:
        throw new UnsupportedOperationException( "not yet implemented for " + getInterestRateProvider().getValidityPeriod() );
    }
  }

  /**
   * Calculates the interest for a interest rate provider with monthly changing interest rates.
   *
   * @param amount the amount
   * @param begin  the begin
   * @param end    the end
   * @return the interest
   */
  @Nonnull
  public Rent calculateInterestMonthlyPeriod( @Nonnull Money amount, @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    List<InterestDetails> interestDetails = new ArrayList<InterestDetails>();

    //Create a runner that represents the begin of the actual month
    //For the first month this may be another day of month than 1
    LocalDate actualMonthBegin = begin;

    //Now iterate over all months
    MutableMoney rentSummer = new MutableMoney();
    while ( actualMonthBegin.isBefore( end ) ) {
      LocalDate actualMonthEnd = actualMonthBegin.plusMonths( 1 );
      //Check if this month is complete --> else only add the rest
      if ( actualMonthEnd.isAfter( end ) ) {
        actualMonthEnd = end;
      }

      double monthYears = getTimeSystem().calculateYears( actualMonthBegin, actualMonthEnd );
      double monthDays = getTimeSystem().calculateDays( actualMonthBegin, actualMonthEnd );
      double monthRate = getInterestRateProvider().getRate( actualMonthBegin );

      //add the rent for the rent
      Money baseForMonth = rentSummer.immutable();

      Money compoundInterest;
      if ( getBackingCalculationSystem().isCompoundSystem() && baseForMonth.getValue() != 0 ) {
        compoundInterest = getBackingCalculationSystem().calculateInterest( baseForMonth, monthYears, monthDays, monthRate );
        rentSummer.plus( compoundInterest );
      } else {
        compoundInterest = Money.ZERO;
      }

      //add the rent for the amount
      Money interest = getBackingCalculationSystem().calculateInterest( amount, monthYears, monthDays, monthRate );
      rentSummer.plus( interest );

      //Build the information object (if expected)
      interestDetails.add( new InterestDetails( actualMonthBegin, actualMonthEnd, monthRate, baseForMonth, interest.plus( compoundInterest ), monthDays ) );

      //Next month --> now we really need the first day of the month
      actualMonthBegin = actualMonthBegin.plusMonths( 1 ).withDayOfMonth( 1 );
    }

    return new Rent( amount, begin, end, interestDetails );
  }

  /**
   * Calculates the interest for a interest rate provider with a constant rate
   *
   * @param amount the amount
   * @param begin  the begin
   * @param end    the end
   * @return the interest
   */
  @Nonnull
  private Rent calculateInterestConstantRate( @Nonnull Money amount, @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    double years = getTimeSystem().calculateYears( begin, end );
    double days = getTimeSystem().calculateDays( begin, end );
    double rate = getInterestRateProvider().getRate( begin );
    Money interest = getBackingCalculationSystem().calculateInterest( amount, years, days, rate );

    //We need just one interest details since the interest rate is constant
    InterestDetails interestDetails = new InterestDetails( begin, begin.plusDays( 1 ), rate, amount, interest, days );//todo check end date
    return new Rent( amount, begin, end, Collections.singletonList( interestDetails ) );
  }

  @Nonnull
  public InterestRateProvider getInterestRateProvider() {
    return interestRateProvider;
  }

  @Nonnull
  public InterestCalculationSystem getBackingCalculationSystem() {
    return backingCalculationSystem;
  }

  @Nonnull
  public TimeSystem getTimeSystem() {
    return timeSystem;
  }
}
