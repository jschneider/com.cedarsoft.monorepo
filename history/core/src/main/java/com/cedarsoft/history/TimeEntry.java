package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

/**
 * An entry for the {@link TimeElementsCollection}
 */
public interface TimeEntry extends Comparable<TimeEntry> {
  @NotNull
  @NonNls
  String PROPERTY_BEGIN = "begin";
  @NotNull
  @NonNls
  String PROPERTY_END = "end";
  @NotNull
  @NonNls
  String PROPERTY_HAS_END = "hasEnd";

  /**
   * Returns the begin of the entry
   *
   * @return the begin
   */
  @NotNull
  LocalDate getBegin();

  /**
   * Returns the end of the entry
   *
   * @return the end (if there is one).
   */
  @Nullable
  LocalDate getEnd();

  /**
   * Sets the end date
   *
   * @param end the end
   */
  void setEnd( @Nullable LocalDate end );

  /**
   * Returns whether the entry has an end set
   *
   * @return whether the entry has an end set
   */
  boolean hasEnd();

  /**
   * Whether the entry is active at the given date
   *
   * @param date the date
   * @return whether the entry is active at the given date
   */
  boolean isActiveAt( @NotNull LocalDate date );
}