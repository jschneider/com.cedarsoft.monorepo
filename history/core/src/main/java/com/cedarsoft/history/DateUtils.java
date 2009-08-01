package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

/**
 * Static class that offers utilty methods for Joda Time
 */
public class DateUtils {

  /**
   * Returns true if the given date is after/same than the begin and before the end
   *
   * @param date  the date
   * @param begin the begin (inclusive)
   * @param end   the end (exclusive)
   * @return whether the given date is between the two given dates
   */

  public static boolean isBetween( @NotNull LocalDate date, @NotNull LocalDate begin, @NotNull LocalDate end ) {
    if ( begin.isAfter( date ) ) {
      return false;
    }
    return date.isBefore( end );
  }
}
