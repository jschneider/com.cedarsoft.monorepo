package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds a list of entries. Each of the entries has an beginning and an (optional) end.
 */
public class TimeElementsCollection<E extends TimeEntry> extends SortedClusteredElementsCollection<E> {
  /**
   * Adds an entry and optionally closes all old entries
   *
   * @param entry           the entry that is added
   * @param closeOldEntries whether the old entries shall be closed
   */
  public void addEntry( @NotNull E entry, boolean closeOldEntries ) {
    if ( closeOldEntries ) {
      for ( E currentEntry : elements ) {
        if ( currentEntry.getEnd() == null ) {
          currentEntry.setEnd( entry.getBegin() );
        }
      }
    }
    addElement( entry );
  }

  /**
   * Returns the latest entry
   *
   * @return the latest entry
   */
  @NotNull
  public E getYoungestEntry() {
    if ( elements.isEmpty() ) {
      throw new IllegalStateException( "No elements found" );
    }
    return elements.get( elements.size() - 1 );
  }

  @NotNull
  public E getOldestEntry() {
    if ( elements.isEmpty() ) {
      throw new IllegalStateException( "No elements found" );
    }
    return elements.get( 0 );
  }

  /**
   * Returns the entry for the given date
   *
   * @param date the date
   * @return the entry
   */
  @NotNull
  public List<? extends E> getEntries( @NotNull LocalDate date ) {
    List<E> found = new ArrayList<E>();

    for ( E cession : elements ) {
      if ( cession.isActiveAt( date ) ) {
        found.add( cession );
      }
    }
    return found;
  }

  /**
   * Returns the begin of the latest cession
   *
   * @return the begin of the latest cession
   */
  @Nullable
  public Date getLatestEntryDate() {
    if ( !hasElements() ) {
      return null;
    }
    return getYoungestEntry().getBegin().toDateTimeAtStartOfDay().toDate();
  }

  @Nullable
  public Date getFirstEntryDate() {
    if ( !hasElements() ) {
      return null;
    }
    return getOldestEntry().getBegin().toDateTimeAtStartOfDay().toDate();
  }
}
