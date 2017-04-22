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

/**
 * Represents a year and a month.
 * January is represented by 1
 */
public class YearMonth implements Comparable<YearMonth> {

  public static final String PROPERTY_YEAR = "year";

  public static final String PROPERTY_MONTH = "month";

  public static final String PROPERTY_DATE = "date";

  private final int year;
  private final int month;

  /**
   * @param year  the year
   * @param month the month (january is 1)
   */
  public YearMonth( int year, int month ) {
    this.year = year;
    this.month = month;
  }

  public YearMonth( @Nonnull ReadableDateTime date ) {
    this( date.getYear(), date.getMonthOfYear() );
  }

  public YearMonth( @Nonnull LocalDate date ) {
    this( date.getYear(), date.getMonthOfYear() );
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  @Nonnull
  public LocalDate getDate() {
    return new LocalDate( year, month, 1 );
  }

  public int compareTo( YearMonth o ) {
    return getYear() * 1000 - o.getYear() * 1000 + getMonth() - o.getMonth();
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    YearMonth yearMonth = ( YearMonth ) o;

    if ( month != yearMonth.month ) return false;
    if ( year != yearMonth.year ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = year;
    result = 31 * result + month;
    return result;
  }

  @Override
  public String toString() {
    return "YearMonth{" +
            "year=" + year +
            ", month=" + month +
            '}';
  }
}
