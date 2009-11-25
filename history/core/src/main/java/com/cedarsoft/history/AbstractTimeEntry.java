package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

/**
 *
 */
public abstract class AbstractTimeEntry implements TimeEntry {
  private Long id;
  @NotNull
  protected LocalDate begin;
  @Nullable
  protected LocalDate end;

  /**
   * Hibernate
   */
  @Deprecated
  protected AbstractTimeEntry() {
  }

  protected AbstractTimeEntry( @NotNull LocalDate begin ) {
    this.begin = begin;
  }

  @Override
  @NotNull
  public LocalDate getBegin() {
    return begin;
  }

  public void setBegin( @NotNull LocalDate begin ) {
    this.begin = begin;
  }

  @Override
  public void setEnd( @Nullable LocalDate end ) {
    this.end = end;
  }

  @Override
  @Nullable
  public LocalDate getEnd() {
    return end;
  }

  @Override
  public boolean hasEnd() {
    return end != null;
  }

  @Override
  public boolean isActiveAt( @NotNull LocalDate date ) {
    if ( !getBegin().isBefore( date ) ) {
      return false;
    }

    LocalDate theEnd = end;
    if ( theEnd == null ) {
      return true;
    } else {
      return theEnd.isAfter( date );
    }
  }

  @Override
  public int compareTo( TimeEntry o ) {
    return getBegin().compareTo( o.getBegin() );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof AbstractTimeEntry ) ) return false;

    AbstractTimeEntry that = ( AbstractTimeEntry ) o;

    if ( !begin.equals( that.begin ) ) return false;
    if ( end != null ? !end.equals( that.end ) : that.end != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = begin.hashCode();
    result = 31 * result + ( end != null ? end.hashCode() : 0 );
    return result;
  }
}
