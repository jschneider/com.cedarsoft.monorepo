package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

/**
 * A entry that is valid at a one point in time
 */
public interface DiscreteHistoryEntry extends HistoryEntry {
  @NonNls
  @NotNull
  String PROPERTY_VALIDITY_DATE = "validityDate";

  /**
   * Returns the validity date of the entry.
   * This is the date this entry is valid at.
   *
   * @return the validity date
   */
  @NotNull
  LocalDate getValidityDate();
}