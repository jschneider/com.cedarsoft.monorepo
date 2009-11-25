package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

/**
 * Default entry for a history with validity dates.
 * Each entry has exactly one validity date where the entry is valid at.
 */
public class DefaultDiscreteHistoryEntry extends DefaultHistoryEntry implements DiscreteHistoryEntry {
  @NotNull
  private final LocalDate validityDate;

  /**
   * Creates a new entry with the current date as validity and verification date
   */
  public DefaultDiscreteHistoryEntry() {
    this( new LocalDate() );
  }

  /**
   * Creates a new entry
   *
   * @param validityDate the validity date
   */
  public DefaultDiscreteHistoryEntry( @NotNull LocalDate validityDate ) {
    this( validityDate, new LocalDate() );
  }

  /**
   * Creates a new entry
   *
   * @param validityDate     the validity date
   * @param verificationDate the verification date
   */
  public DefaultDiscreteHistoryEntry( @NotNull LocalDate validityDate, @NotNull LocalDate verificationDate ) {
    super( verificationDate );
    this.validityDate = validityDate;
  }

  @Override
  @NotNull
  public LocalDate getValidityDate() {
    return validityDate;
  }
}
