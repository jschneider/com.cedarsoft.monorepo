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

import javax.annotation.Nonnull;
import org.joda.time.LocalDate;
import org.joda.time.ReadableDateTime;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MappedInterestRateProvider implements InterestRateProvider {
  private double defaultRate = 0.045;

  @Nonnull
  private final Map<YearMonth, Double> rateMapping = new HashMap<YearMonth, Double>();

  public MappedInterestRateProvider( double defaultRate ) {
    this.defaultRate = defaultRate;
  }

  public void setRate( double rate, @Nonnull ReadableDateTime date ) {
    setRate( rate, new YearMonth( date ) );
  }

  public void setRate( double rate, @Nonnull LocalDate date ) {
    setRate( rate, new YearMonth( date ) );
  }

  public void setRate( double rate, @Nonnull YearMonth yearMonth ) {
    Double oldRate = rateMapping.get( yearMonth );
    //noinspection FloatingPointEquality
    if ( oldRate != null && oldRate != rate ) {
      throw new IllegalStateException( "Trying to overwrite " + oldRate + " with " + rate + " for " + yearMonth );
    }
    rateMapping.put( yearMonth, rate );
  }

  @Override
  public double getRate(@Nonnull LocalDate date ) {
    return getRate( new YearMonth( date ) );
  }

  @Override
  @Nonnull
  public ValidityPeriod getValidityPeriod() {
    return ValidityPeriod.MONTHLY;
  }

  public double getRate( @Nonnull YearMonth yearMonth ) {
    Double rate = rateMapping.get( yearMonth );
    if ( rate != null ) {
      return rate;
    } else {
      return defaultRate;
    }
  }

  public double getRate( int year, int month ) {
    return getRate( new YearMonth( year, month ) );
  }

  public double getRate( @Nonnull ReadableDateTime date ) {
    return getRate( new YearMonth( date.getYear(), date.getMonthOfYear() ) );
  }

  public void setDefaultRate( double defaultRate ) {
    this.defaultRate = defaultRate;
  }

  public double getDefaultRate() {
    return defaultRate;
  }

  public int getEntriesCount() {
    return rateMapping.size();
  }
}
