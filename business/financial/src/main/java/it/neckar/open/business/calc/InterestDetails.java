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

import javax.annotation.Nonnull;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

/**
 * Contains interest information within a given interval.
 */
public class InterestDetails {

  public static final String PROPERTY_INTERVAL = "interval";

  public static final String PROPERTY_BEGIN = "begin";

  public static final String PROPERTY_END = "end";

  public static final String PROPERTY_INTEREST = "interest";

  public static final String PROPERTY_BASE_VALUE = "baseValue";

  public static final String PROPERTY_DAYS = "days";

  public static final String PROPERTY_RATE = "rate";

  @Nonnull
  private final Money baseValue;
  @Nonnull
  private final Money interest;
  private final double days;
  @Nonnull
  private final LocalDate begin;
  @Nonnull
  private final LocalDate end;
  private final double rate;

  /**
   * Creates a new instance
   *
   * @param begin
   * @param end
   * @param rate      the rate for this period
   * @param baseValue the baseValue for this period
   * @param interest  the interest for this period
   * @param days      the amount of days of this period (depending on the time system!)
   */
  public InterestDetails( @Nonnull LocalDate begin, @Nonnull LocalDate end, double rate, @Nonnull Money baseValue, @Nonnull Money interest, double days ) {
    this.begin = begin;
    this.end = end;
    this.rate = rate;
    this.baseValue = baseValue;
    this.interest = interest;
    this.days = days;
  }

  @Nonnull
  public Money getBaseValue() {
    return baseValue;
  }

  public double getDays() {
    return days;
  }

  @Nonnull
  public LocalDate getBegin() {
    return begin;
  }

  @Nonnull
  public LocalDate getEnd() {
    return end;
  }

  @Nonnull
  public Money getInterest() {
    return interest;
  }

  public double getRateAsDouble() {
    return rate;
  }

  @Nonnull
  public BigDecimal getRate() {
    return new BigDecimal( rate );
  }

  @Override
  public String toString() {
    return "InterestDetails{" +
        "interest=" + interest +
        ", days=" + days +
        ", rate=" + rate +
        ", baseValue=" + baseValue +
        ", begin=" + begin +
        ", end=" + end +
        '}';
  }
}
