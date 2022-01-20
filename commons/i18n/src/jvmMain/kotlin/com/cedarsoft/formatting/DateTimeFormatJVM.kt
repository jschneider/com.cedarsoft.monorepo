package com.cedarsoft.formatting

import com.cedarsoft.commons.time.DateUtils
import com.cedarsoft.i18n.I18nConfiguration
import com.cedarsoft.i18n.convert
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * A format that does not show the time zone nor the offset
 */
private val utcDateTimeFormat = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS")

actual class DateFormatIso8601 : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    return DateUtils.toZonedDateTime(timestamp.toLong(), i18nConfiguration.timeZone.toZoneId()).format(DateTimeFormatter.ISO_DATE_TIME)
  }
}

actual class DateFormatUTC : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    return DateUtils.toOffsetDateTime(timestamp.toLong(), i18nConfiguration.timeZone.toZoneId()).withOffsetSameInstant(ZoneOffset.UTC).format(utcDateTimeFormat)
  }
}

actual class TimeFormat : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).withLocale(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }
}

actual class TimeFormatWithMillis : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return DateUtils.createTimeMillisFormat(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }
}

actual class DateFormat : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }
}

actual class DefaultDateTimeFormat : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }
}

actual class DateTimeFormatShort : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }
}

actual class DateTimeFormatWithMillis : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return DateUtils.createDateTimeMillisFormat(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }
}

actual class DateTimeFormatShortWithMillis : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return DateUtils.createDateTimeShortMillisFormat(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }
}

/**
 * A format that formats a date - but only prints the month and year
 */
actual class YearMonthFormat actual constructor() : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())
    return monthYearFormat.withLocale(i18nConfiguration.formatLocale.convert()).format(dateTime)
  }

  companion object {
    val monthYearFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
  }
}

/**
 * Formats a time stamp as second with millis
 */
actual class SecondMillisFormat actual constructor() : DateTimeFormat {
  override fun format(timestamp: Double, i18nConfiguration: I18nConfiguration): String {
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), i18nConfiguration.timeZone.toZoneId())

    val value = dateTime.second + dateTime.nano / 1_000_000_000.0
    return decimalFormat(3).format(value, i18nConfiguration)
  }
}
