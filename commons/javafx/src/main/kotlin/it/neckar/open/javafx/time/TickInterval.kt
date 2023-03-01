package it.neckar.open.javafx.time

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

/**
 * Represents an interval for the time/date axis
 *
 */
enum class TickInterval(
  val interval: ChronoUnit,
  val amount: Int,
) {
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

  /**
   * Formats the given date depending on the interval
   */
  fun format(date: LocalDateTime): String {
    val dateTimeFormat: DateTimeFormatter
    val calendar = LocalDateTime.of(date.toLocalDate(), date.toLocalTime())
    dateTimeFormat = if (interval == ChronoUnit.YEARS && calendar.monthValue == 1 && calendar.dayOfMonth == 1) {
      DateTimeFormatter.ofPattern("yyyy")
    } else if (interval == ChronoUnit.MONTHS && calendar.dayOfMonth == 1) {
      DateTimeFormatter.ofPattern("MMMM yy")
    } else {
      when (interval) {
        ChronoUnit.DAYS, ChronoUnit.WEEKS -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        ChronoUnit.HOURS, ChronoUnit.MINUTES -> DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)
        ChronoUnit.SECONDS -> DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)
        ChronoUnit.MILLIS -> DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)
        else -> DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
      }
    }
    return dateTimeFormat.format(date)
  }
}
