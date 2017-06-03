package com.cedarsoft.commons.time;

import com.cedarsoft.unit.si.ms;
import com.cedarsoft.unit.si.ns;
import com.cedarsoft.unit.si.s;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DateUtil {
  /**
   * Pattern to format a duration as HH:mm
   */
  @Nonnull
  public static final String PATTERN_HH_MM = "HH:mm";

  /**
   * Converts the millis to a local date with the given zone
   *
   * @param millis the milli seconds
   * @return the local date for the given zone id
   */
  @Nonnull
  public static LocalDate toLocalDate(long millis, @Nonnull ZoneId zone) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), zone).toLocalDate();
  }

  @Nonnull
  public static ZonedDateTime toZonedDateTime(long millis) {
    return toZonedDateTime(millis, ZoneId.systemDefault());
  }

  @Nonnull
  public static ZonedDateTime toZonedDateTime(long millis, @Nonnull ZoneId zone) {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zone);
  }

  @Nonnull
  public static String toHumanReadableDuration(@ms long millis) {
    return DurationFormatUtils.formatDurationWords(millis, true, true);
  }

  /**
   * Formats hour, minute, seconds, millis
   */
  @Nonnull
  public static String formatHMSM(@ms long millis) {
    return DurationFormatUtils.formatDurationHMS(millis);
  }

  /**
   * Formats hour, minute, seconds
   */
  @Nonnull
  public static String formatHMS(@ms long millis) {
    return DurationFormatUtils.formatDuration(millis, "HH:mm:ss");
  }

  @s
  private static final long SECONDS_PER_HOUR = 60 * 60;
  @s
  private static final long SECONDS_PER_MINUTE = 60;

  @Nonnull
  public static String formatDurationWords(@ms Duration duration) {
    return formatDurationWords(duration.toMillis());
  }

  public static String formatDurationWords(@ms long millis) {
    StringBuilder sb = new StringBuilder();

    @s long seconds = millis / 1000; // skip milliseconds

    if (seconds >= SECONDS_PER_HOUR || sb.length() > 0) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      long hours = seconds / SECONDS_PER_HOUR;
      sb.append(NumberFormat.getNumberInstance().format(hours)).append("h");
      seconds -= hours * SECONDS_PER_HOUR;
    }

    NumberFormat twoDigitsFormat = NumberFormat.getNumberInstance();
    //always use two digits if the hour has been set
    if (sb.length() > 0) {
      twoDigitsFormat.setMinimumIntegerDigits(2);
    }

    if (seconds >= SECONDS_PER_MINUTE || sb.length() > 0) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      long minutes = seconds / SECONDS_PER_MINUTE;

      sb.append(twoDigitsFormat.format(minutes)).append("min");
      seconds -= minutes * SECONDS_PER_MINUTE;
    }

    if (sb.length() > 0) {
      sb.append(" ");
    }
    //always use two digits if the minute has been set
    if (sb.length() > 0) {
      twoDigitsFormat.setMinimumIntegerDigits(2);
    }

    sb.append(twoDigitsFormat.format(seconds));
    sb.append("s");

    return sb.toString();
  }

  /**
   * Returns all nanos from instant
   *
   * @param instant instant to convert in nanos
   * @return nanos from epoch
   */
  @ns
  public static long toNanos(@Nonnull Instant instant) {
    return TimeUnit.SECONDS.toNanos(instant.getEpochSecond()) + instant.getNano();
  }

  @Nonnull
  public static String formatDurationHHmm(@Nonnull Duration duration) {
    return DurationFormatUtils.formatDuration(duration.toMillis(), PATTERN_HH_MM);
  }

  @Nonnull
  public static Duration parseDurationHHmm(@Nonnull String formatted) {
    int index = formatted.indexOf(':');
    if (index < 0) {
      throw new IllegalArgumentException("Could not parse <" + formatted + ">");
    }

    String firstPart = formatted.substring(0, index);
    String secondPart = formatted.substring(index + 1);

    return Duration
      .ofHours(Long.parseLong(firstPart))
      .plusMinutes(Long.parseLong(secondPart));
  }
}
