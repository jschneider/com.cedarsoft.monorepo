package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

import java.lang.Override;

/**
 * Contains information that are valid at a given point in time.
 */
public class DefaultHistoryEntry implements HistoryEntry {
  private Long id;

  @NotNull
  private final LocalDate verificationDate;

  /**
   * Creates a new default entry with the current datetime as verification date
   */
  public DefaultHistoryEntry() {
    this( new LocalDate() );
  }

  /**
   * Creates a new default entry
   *
   * @param verificationDate the verification date
   */
  public DefaultHistoryEntry( @NotNull LocalDate verificationDate ) {
    this.verificationDate = verificationDate;
  }

  @Override
  @NotNull
  public LocalDate getVerificationDate() {
    return verificationDate;
  }

  //todo really necessary????
  @Override
  public int compareTo( @NotNull HistoryEntry o ) {
    return verificationDate.compareTo( o.getVerificationDate() );
  }

  /**
   * Use with care!
   *
   * @return the id
   */
  @Nullable
  public Long getId() {
    return id;
  }
}
