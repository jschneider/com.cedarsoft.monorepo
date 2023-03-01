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

import it.neckar.open.business.Money;
import it.neckar.open.business.MutableMoney;
import it.neckar.open.business.calc.TimeSystem;
import it.neckar.open.business.payment.DefaultPayment;
import it.neckar.open.business.payment.Payment;

import javax.annotation.Nonnull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Calculates the PAngV 2000.
 */
public class PAngV2000Calculator {
  @Nonnull
  public static final Money TARGET_DELTA = new Money(0.005);
  public static final double INITIAL_RATE_GUESS = 0.5;
  public static final double MIN_CHANGE = 0.000004;

  /**
   * The payments
   */
  private final List<Payment> payments = new ArrayList<Payment>();

  /**
   * The base date for the calculation
   */
  @Nonnull
  private final LocalDate baseDate;

  /**
   * Creates a new pang calculator for the given base date
   *
   * @param baseDate the base date
   */
  public PAngV2000Calculator( @Nonnull LocalDate baseDate ) {
    this.baseDate = baseDate;
  }

  /**
   * Adds a (default) payment.
   *
   * @param amount the amount
   * @param date   the date
   */
  public void addPayment( @Nonnull Money amount, @Nonnull LocalDate date ) {
    addPayment(new DefaultPayment(amount, date));
  }

  /**
   * Adds a payment
   *
   * @param payment the payment
   */
  public void addPayment( @Nonnull Payment payment ) {
    payments.add( payment );
  }

  /**
   * Adds the given payments
   *
   * @param additionalPayments the payments
   */
  public void addPayments( @Nonnull Collection<? extends Payment> additionalPayments ) {
    this.payments.addAll( additionalPayments );
  }

  /**
   * Returns the amount of the payments
   *
   * @return the amount of payments
   */
  @Nonnull
  public Money getPaymentsAmount() {
    MutableMoney amount = new MutableMoney();
    for ( Payment payment : payments ) {
      amount.plus( payment.getAmount() );
    }
    return amount.getMoney();
  }

  /**
   * Returns the payments
   *
   * @return
   */
  @Nonnull
  public List<? extends Payment> getPayments() {
    return Collections.unmodifiableList( payments );
  }

  /**
   * Calculates the PangV
   *
   * @return the PangV
   */
  @Nonnull
  public PangV calculate() {
    if ( payments.size() < 2 ) {
      return PangV.NULL;
    }

    MutableMoney sum = new MutableMoney();
    for ( Payment payment : payments ) {
      sum.plus( payment.getAmount() );
    }

    boolean sumPositive = sum.getEstimatedValue() > 0;

    //test it using bisection

    double actualGuess;
    if ( sumPositive ) {
      actualGuess = -INITIAL_RATE_GUESS;
    } else {
      actualGuess = INITIAL_RATE_GUESS;
    }

    int counter = 0;

    Money actualValue = Money.BIG;
    while ( actualValue.abs().isGreaterThan( TARGET_DELTA ) ) {
      double change = INITIAL_RATE_GUESS / Math.pow( 2, counter );

      if ( change < MIN_CHANGE ) {
        return new PangV( actualGuess, actualValue, counter, sum.immutable() );
      }

      //modify guess
      if ( !sumPositive ^ actualValue.isGreaterThan( Money.ZERO ) ) {
        actualGuess += change;
      } else {
        actualGuess -= change;
      }

      actualValue = calculateCashValue( actualGuess );
      counter++;
    }

    return new PangV( actualGuess, actualValue, counter, sum.immutable() );
  }

  /**
   * Calculates the cash value for all payments
   *
   * @param rate the rate
   * @return the cash value for all payments
   */
  @Nonnull
  public Money calculateCashValue( double rate ) {
    MutableMoney cashValue = new MutableMoney();
    for ( Payment payment : payments ) {
      cashValue.plus( calulateCashValue( baseDate, payment, rate ) );
    }
    return cashValue.immutable();
  }

  /**
   * Calculates the cash value of a payment for a given rate.
   * The base for this calculation is a year with 365 days
   *
   * @param payment
   * @param rate    the rate
   * @return the value
   */
  @Nonnull
  public static Money calulateCashValue( @Nonnull LocalDate base, @Nonnull Payment payment, double rate ) {
    double years = TimeSystem.BANK_304_365.calculateYears( base, payment.getDate() );
    double divider = Math.pow( 1 + rate, years );
    return payment.getAmount().divide( divider );
  }
}
