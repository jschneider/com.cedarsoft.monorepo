package com.cedarsoft.commons.javafx.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;

/**
 * Represents an interval for the time/date axis
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public enum TickInterval {
  DECADE(ChronoUnit.DECADES, 1),
  YEAR(ChronoUnit.YEARS, 1),
  MONTH_6(ChronoUnit.MONTHS, 6),
  MONTH_3(ChronoUnit.MONTHS, 3),
  MONTH_1(ChronoUnit.MONTHS, 1),
  WEEK(ChronoUnit.WEEKS, 1),
  DAY(ChronoUnit.DAYS, 1),
  HOUR_12(ChronoUnit.HOURS, 12),
  HOUR_6(ChronoUnit.HOURS, 6),
  HOUR_3(ChronoUnit.HOURS, 3),
  HOUR_1(ChronoUnit.HOURS, 1),
  MINUTE_15(ChronoUnit.MINUTES, 15),
  MINUTE_5(ChronoUnit.MINUTES, 5),
  MINUTE_1(ChronoUnit.MINUTES, 1),
  SECOND_15(ChronoUnit.SECONDS, 15),
  SECOND_5(ChronoUnit.SECONDS, 5),
  SECOND_1(ChronoUnit.SECONDS, 1),
  MILLISECOND(ChronoUnit.MILLIS, 1);

  @Nonnull
  private final ChronoUnit interval;
  private final int amount;

  TickInterval(@Nonnull ChronoUnit interval, final int amount) {
    this.interval = interval;
    this.amount = amount;
  }

  @Nonnull
  public ChronoUnit getInterval() {
    return interval;
  }

  public int getAmount() {
    return amount;
  }

  /**
   * Formats the given date depending on the interval
   */
  @Nonnull
  public String format(@Nonnull LocalDateTime date) {
    DateTimeFormatter dateTimeFormat;
    LocalDateTime calendar = LocalDateTime.of(date.toLocalDate(), date.toLocalTime());

    if (getInterval() == ChronoUnit.YEARS && calendar.getMonthValue() == 1 && calendar.getDayOfMonth() == 1) {
      dateTimeFormat = DateTimeFormatter.ofPattern("yyyy");
    }
    else if (getInterval() == ChronoUnit.MONTHS && calendar.getDayOfMonth() == 1) {
      dateTimeFormat = DateTimeFormatter.ofPattern("MMMM yy");
    }
    else {
      switch (getInterval()) {
        case DAYS:
        case WEEKS:
        default:
          dateTimeFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
          break;
        case HOURS:
        case MINUTES:
          dateTimeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
          break;
        case SECONDS:
          dateTimeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
          break;
        case MILLIS:
          dateTimeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
          break;
      }
    }

    return dateTimeFormat.format(date);

  }
}
