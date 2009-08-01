package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.joda.time.LocalDate;

/**
 * An entry for a history
 */
public interface HistoryEntry extends Comparable<HistoryEntry> {
  @NonNls
  @NotNull
  String PROPERTY_VERIFICATION_DATE = "verificationDate";

  /**
   * The point in time when the information stored within this entry, has been verified
   *
   * @return the point in time when the information stored within this entry, has been verified
   */
  @NotNull
  LocalDate getVerificationDate();
}