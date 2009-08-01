package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.LocalDate;

/**
 * A continuous entry.
 */
public interface ContinuousEntry extends Comparable<ContinuousEntry> {
  @NotNull
  @NonNls
  String PROPERTY_BEGIN = "begin";

  /**
   * Returns the begin of the interval this entry describes
   *
   * @return the begin date of the interval this entry describes
   */
  @NotNull
  LocalDate getBegin();

  /**
   * Sets the *new* begin for the entry
   * Use this method with care! Instead try to use the methods of
   * {@link ContinuousEntriesInformation}
   *
   * @param begin the new begin
   */
  void setBegin( @NotNull LocalDate begin );
}