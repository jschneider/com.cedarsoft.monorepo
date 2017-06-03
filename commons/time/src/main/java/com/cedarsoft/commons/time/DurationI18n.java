package com.cedarsoft.commons.time;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Contains the strings for duration formatting
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public enum DurationI18n {
  ENGLISH(Locale.ENGLISH, "days", "hours", "minutes", "seconds", "day", "hour", "minute", "second"),
  GERMAN(Locale.GERMAN, "Tage", "Stunden", "Minuten", "Sekunden", "Tag", "Stunde", "Minute", "Sekunde");

  @Nonnull
  private final Locale language;

  @Nonnull
  private final String daysString;
  @Nonnull
  private final String hoursString;
  @Nonnull
  private final String minutesString;
  @Nonnull
  private final String secondsString;

  @Nonnull
  private final String dayString;
  @Nonnull
  private final String hourString;
  @Nonnull
  private final String minuteString;
  @Nonnull
  private final String secondString;

  /**
   * @noinspection ConstructorWithTooManyParameters
   */
  DurationI18n(@Nonnull Locale language, @Nonnull String daysString, @Nonnull String hoursString, @Nonnull String minutesString, @Nonnull String secondsString, @Nonnull String dayString, @Nonnull String hourString, @Nonnull String minuteString, @Nonnull String secondString) {
    this.language = language;
    this.daysString = daysString;
    this.hoursString = hoursString;
    this.minutesString = minutesString;
    this.secondsString = secondsString;
    this.dayString = dayString;
    this.hourString = hourString;
    this.minuteString = minuteString;
    this.secondString = secondString;
  }

  @Nonnull
  public String getDaysString() {
    return daysString;
  }

  @Nonnull
  public String getHoursString() {
    return hoursString;
  }

  @Nonnull
  public String getMinutesString() {
    return minutesString;
  }

  @Nonnull
  public String getSecondsString() {
    return secondsString;
  }

  @Nonnull
  public String getDayString() {
    return dayString;
  }

  @Nonnull
  public String getHourString() {
    return hourString;
  }

  @Nonnull
  public String getMinuteString() {
    return minuteString;
  }

  @Nonnull
  public String getSecondString() {
    return secondString;
  }

  @Nonnull
  public Locale getLanguage() {
    return language;
  }

  /**
   * Returns the best duration i18n for the given language. Will return {@link #ENGLISH} as fallback
   */
  @Nonnull
  public static DurationI18n get(@Nonnull Locale language) {
    for (DurationI18n durationI18n : values()) {
      if (durationI18n.getLanguage().getLanguage().equals(language.getLanguage())) {
        return durationI18n;
      }
    }

    return ENGLISH;
  }
}
