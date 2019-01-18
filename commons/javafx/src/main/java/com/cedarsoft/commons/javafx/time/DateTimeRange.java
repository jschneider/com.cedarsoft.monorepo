package com.cedarsoft.commons.javafx.time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.cedarsoft.unit.other.pct;
import com.cedarsoft.unit.si.ms;

/**
 * Represents a range between two date times
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Immutable
public class DateTimeRange {
  /**
   * Represents no time
   */
  @Nonnull
  public static final LocalDateTime NO_TIME = LocalDateTime.ofInstant(Instant.EPOCH, ZoneId.systemDefault());
  @Nonnull
  public static final DateTimeRange NONE = new DateTimeRange(NO_TIME, NO_TIME);
  /**
   * The earliest supported date. All dates before are invalid
   */
  public static final LocalDateTime EARLIEST_DATE = LocalDateTime.of(1980, 1, 1, 0, 0, 0);


  @Nonnull
  private final LocalDateTime start;
  @Nonnull
  private final LocalDateTime end;

  public DateTimeRange(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end) {
    this.start = start;
    this.end = end;
  }

  @ms
  public static long toMillis(@Nonnull ChronoLocalDateTime<LocalDate> localDateTime) {
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  @Nonnull
  public static DateTimeRange create(@Nonnull LocalDateTime start, @Nonnull Duration duration) {
    return new DateTimeRange(start, start.plus(duration));
  }

  @Nonnull
  public LocalDateTime getStart() {
    return start;
  }

  @Nonnull
  public LocalDateTime getEnd() {
    return end;
  }

  @ms
  public long getStartAsMillis() {
    return DateTimeRange.toMillis(getStart());
  }

  @ms
  public long getEndAsMillis() {
    return DateTimeRange.toMillis(getEnd());
  }

  @Nonnull
  public Duration getDuration() {
    return Duration.between(start, end);
  }

  /**
   * Returns the time at the given percentage between start and end
   */
  @Nonnull
  public LocalDateTime calcluateTimeAt(@pct double percentage) {
    Duration duration = getDuration();
    @ms double millis = duration.toMillis() * percentage;
    return start.plus((long) millis, ChronoUnit.MILLIS);
  }

  /**
   * Returns the time at the center
   */
  @Nonnull
  public LocalDateTime getCenter() {
    Duration duration = getDuration();
    return start.plus(duration.dividedBy(2));
  }

  /**
   * Returns a new DateTimeRange moved by the given deltas
   */
  @Nonnull
  public DateTimeRange plus(@ms long deltaMillis) {
    LocalDateTime newStart = getStart().plus(deltaMillis, ChronoUnit.MILLIS);
    LocalDateTime newEnd = getEnd().plus(deltaMillis, ChronoUnit.MILLIS);

    return new DateTimeRange(newStart, newEnd);
  }

  /**
   * Creates a new date range with the same duration but the center at the given position
   */
  @Nonnull
  public DateTimeRange withCenterAt(@Nonnull LocalDateTime center) {
    Duration duration = getDuration();

    LocalDateTime start = center.minus(duration.dividedBy(2));
    LocalDateTime plus = center.plus(duration.dividedBy(2));

    return new DateTimeRange(start, plus);
  }

  @Override
  public String toString() {
    return "DateTimeRange{" +
             "start=" + start +
             ", end=" + end +
             '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DateTimeRange that = (DateTimeRange) o;
    return Objects.equals(start, that.start) &&
             Objects.equals(end, that.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }
}
