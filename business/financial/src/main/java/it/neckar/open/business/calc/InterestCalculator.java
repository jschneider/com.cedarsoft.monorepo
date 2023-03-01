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

package it.neckar.open.business.calc;

import it.neckar.open.business.Money;
import it.neckar.open.business.MutableMoney;
import it.neckar.open.business.payment.Payment;

import javax.annotation.Nonnull;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Calculates interests and related numbers
 */
public class InterestCalculator {
  private double interestRate;

  @Nonnull
  private final List<Payment> payments = new ArrayList<Payment>();
  @Nonnull
  private final InterestCalculationSystem calculationSystem;
  @Nonnull
  private final TimeSystem timeSystem;

  /**
   * Creates a new interest calculator.
   * It is necessary to set the interest rate ({@link #setInterestRate(double)})
   *
   * @param calculationSystem the calculation system
   * @param timeSystem        the time system
   */
  @Deprecated
  public InterestCalculator( @Nonnull InterestCalculationSystem calculationSystem, @Nonnull TimeSystem timeSystem ) {
    this.calculationSystem = calculationSystem;
    this.timeSystem = timeSystem;
  }

  /**
   * Creates a new interest calculator
   *
   * @param calculationSystem the calculation system
   * @param timeSystem        the time system
   * @param interestRate      the interest rate
   */
  public InterestCalculator( @Nonnull InterestCalculationSystem calculationSystem, @Nonnull TimeSystem timeSystem, double interestRate ) {
    this.calculationSystem = calculationSystem;
    this.timeSystem = timeSystem;
    this.interestRate = interestRate;
  }

  /**
   * Sets the interest rate
   *
   * @param interestRate the interest rate
   */
  public void setInterestRate( double interestRate ) {
    this.interestRate = interestRate;
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
   * Returns the interest rate
   *
   * @return the rate
   */
  public double getInterestRate() {
    return interestRate;
  }

  /**
   * Calculates the interest until the given date.
   * This method requires that at least one payment {@link #addPayment(Payment)}
   * and the interest rate {@link #setInterestRate(double)} are set
   *
   * @param until the end date
   * @return the interest
   */
  @Nonnull
  public Money calculateInterestUntil(@Nonnull LocalDate until) {
    if (payments.isEmpty()) {
      throw new IllegalStateException("Need at least one payment");
    }
    if (interestRate == 0) {
      throw new IllegalStateException("Interest rate not set");
    }

    MutableMoney sum = new MutableMoney();

    for (Payment payment : payments) {
      double years = timeSystem.calculateYears( payment.getDate(), until );
      double days = timeSystem.calculateDays( payment.getDate(), until );
      sum.plus( calculationSystem.calculateInterest( payment.getAmount(), years, days, interestRate ) );
    }

    return sum.immutable();
  }

  /**
   * Returns the payments
   *
   * @return the payments
   */
  @Nonnull
  public List<Payment> getPayments() {
    return Collections.unmodifiableList( payments );
  }

  /**
   * Returns the calculation system
   *
   * @return the calculation system
   */
  @Nonnull
  public InterestCalculationSystem getCalculationSystem() {
    return calculationSystem;
  }

  /**
   * Returns the time system
   *
   * @return the time system
   */
  @Nonnull
  public TimeSystem getTimeSystem() {
    return timeSystem;
  }
}
