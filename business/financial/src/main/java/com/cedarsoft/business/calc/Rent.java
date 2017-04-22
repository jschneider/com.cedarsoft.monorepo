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
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the rent information about an amount
 */
public class Rent {

  public static final String PROPERTY_UNTIL = "until";

  public static final String PROPERTY_VALUE_DATE = "valueDate";

  public static final String PROPERTY_INTEREST = "interest";

  public static final String PROPERTY_SUM = "sum";

  public static final String PROPERTY_DURATION = "duration";

  public static final String PROPERTY_AMOUNT = "amount";

  public static final String PROPERTY_INTEREST_DETAILS = "interestDetails";
  @Nonnull
  protected final List<? extends InterestDetails> interestDetails;

  @Nonnull
  protected final Money amount;
  @Nonnull
  protected final Money interest;
  @Nonnull
  protected final LocalDate until;
  @Nonnull
  protected final LocalDate valueDate;

  /**
   * Creates a new rent information
   *
   * @param amount          the amount
   * @param valueDate       the value date of the amount
   * @param until           the date the interest is calculated until
   * @param interestDetails the interest details
   */
  public Rent( @Nonnull Money amount, @Nonnull LocalDate valueDate, @Nonnull LocalDate until, @Nonnull List<? extends InterestDetails> interestDetails ) {
    this.amount = amount;
    this.until = until;
    this.valueDate = valueDate;
    this.interestDetails = new ArrayList<InterestDetails>( interestDetails );

    //Sum up
    MutableMoney interestSummer = new MutableMoney();
    for ( InterestDetails interestDetail : interestDetails ) {
      interestSummer.plus( interestDetail.getInterest() );
    }
    this.interest = interestSummer.immutable();
  }

  @Nonnull
  public Period getDuration() {
    return new Period( valueDate, until );
  }

  @Nonnull
  public Money getAmount() {
    return amount;
  }

  @Nonnull
  public Money getSum() {
    return amount.plus( interest );
  }

  @Nonnull
  public LocalDate getUntil() {
    return until;
  }

  @Nonnull
  public List<? extends InterestDetails> getInterestDetails() {
    return Collections.unmodifiableList( interestDetails );
  }

  @Nonnull
  public LocalDate getValueDate() {
    return valueDate;
  }

  @Nonnull
  public Money getInterest() {
    return interest;
  }


  @Override
  public String toString() {
    return "Rent{" +
        "amount=" + amount +
        ", interest=" + interest +
        ", until=" + until +
        ", valueDate=" + valueDate +
        '}';
  }
}
