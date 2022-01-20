package com.cedarsoft.formatting

import com.cedarsoft.charting.algorithms.time.TimeConstants
import com.cedarsoft.common.kotlin.lang.toIntFloor
import com.cedarsoft.i18n.I18nConfiguration
import com.cedarsoft.unit.si.ms

/**
 * Formats a date and/or time
 */
interface DateTimeFormat {
  /**
   * Formats a date to a string
   */
  fun format(@ms timestamp: Double, i18nConfiguration: I18nConfiguration): String
}

/**
 * Formats a date in accordance to the ISO format 8601
 */
val dataFormatIso8601: CachedDateTimeFormat = DateFormatIso8601().cached()

/**
 * Formats a date as a UTC date (locale independent)
 */
val dateFormatUTC: CachedDateTimeFormat = DateFormatUTC().cached()

/**
 * A formatted date (time only)
 */
val timeFormat: CachedDateTimeFormat = TimeFormat().cached()

/**
 * A formatted time including milli seconds
 */
val timeFormatWithMillis: CachedDateTimeFormat = TimeFormatWithMillis().cached()

/**
 * A format for a date (without time)
 */
val dateFormat: CachedDateTimeFormat = DateFormat().cached()

/**
 * Prints the year and months
 */
val yearMonthFormat: CachedDateTimeFormat = YearMonthFormat().cached()

/**
 * Formats the second and milli second
 */
val secondMillisFormat: CachedDateTimeFormat = SecondMillisFormat().cached()

/**
 * A format for a date and a time
 */
val dateTimeFormat: CachedDateTimeFormat = DefaultDateTimeFormat().cached()

/**
 * A format for a date and a time (short date format)
 */
val dateTimeFormatShort: CachedDateTimeFormat = DateTimeFormatShort().cached()

/**
 * A format for a date and a time with millis
 */
val dateTimeFormatWithMillis: CachedDateTimeFormat = DateTimeFormatWithMillis().cached()

/**
 * A format for a date and a time with millis (short date format)
 */
val dateTimeFormatShortWithMillis: CachedDateTimeFormat = DateTimeFormatShortWithMillis().cached()

/**
 * Formats a date in accordance to the ISO format 8601
 */
expect class DateFormatIso8601() : DateTimeFormat

/**
 * Formats a date as a UTC date (locale independent)
 */
expect class DateFormatUTC() : DateTimeFormat

/**
 * A formatted date (time only)
 */
expect class TimeFormat() : DateTimeFormat

/**
 * A formatted time including milli seconds
 */
expect class TimeFormatWithMillis() : DateTimeFormat

/**
 * A format for a date (without time)
 */
expect class DateFormat() : DateTimeFormat

/**
 * A format that formats a date - but only prints the month and year
 */
expect class YearMonthFormat() : DateTimeFormat

/**
 * Formats a time stamp as second with millis
 */
expect class SecondMillisFormat() : DateTimeFormat

/**
 * A format for a date and a time
 */
expect class DefaultDateTimeFormat() : DateTimeFormat

/**
 * A format for a date and a time (short date format)
 */
expect class DateTimeFormatShort() : DateTimeFormat

/**
 * A format for a date and a time with millis
 */
expect class DateTimeFormatWithMillis() : DateTimeFormat

/**
 * A format for a date and a time with millis (short date format)
 */
expect class DateTimeFormatShortWithMillis() : DateTimeFormat


/**
 * Formats millis as UTC
 */
fun @ms Double.formatUtc(): String {
  if (this.isNaN()) {
    return "NaN"
  }

  if (this.isInfinite()) {
    return "∞"
  }

  return try {
    dateFormatUTC.format(this, I18nConfiguration.GermanyUTC)
  } catch (e: Throwable) {
    "---${this}---[$e]"
  }
}

/**
 * Formats this (interpreted as milliseconds) as human readable duration
 */
fun @ms Double.formatAsDuration(): String {
  if (this.isNaN()) {
    return "NaN"
  }

  if (this.isInfinite()) {
    return "∞"
  }


  var days: Int = 0
  var hours: Int = 0
  var minutes: Int = 0
  var seconds: Int = 0
  var milliseconds: Double = this

  if (true) {
    days = (milliseconds / TimeConstants.millisPerDay).toIntFloor()
    milliseconds = milliseconds - days * TimeConstants.millisPerDay
  }
  if (true) {
    hours = (milliseconds / TimeConstants.millisPerHour).toIntFloor()
    milliseconds = milliseconds - hours * TimeConstants.millisPerHour
  }
  if (true) {
    minutes = (milliseconds / TimeConstants.millisPerMinute).toIntFloor()
    milliseconds = milliseconds - minutes * TimeConstants.millisPerMinute
  }
  if (true) {
    seconds = (milliseconds / TimeConstants.millisPerSecond).toIntFloor()
    milliseconds = milliseconds - seconds * TimeConstants.millisPerSecond
  }

  return buildString {
    var addedSomething = false

    if (addedSomething || days != 0) {
      append("${intFormat.format(days.toDouble())} days ")
      addedSomething = true
    }
    if (addedSomething || hours != 0) {
      append("${hours}h ")
      addedSomething = true
    }
    if (addedSomething || minutes != 0) {
      append("${minutes}min ")
      addedSomething = true
    }
    if (addedSomething || seconds != 0) {
      append("${seconds}s ")
      addedSomething = true
    }

    append("${milliseconds}ms")
  }
}
