package com.cedarsoft.history;

import com.cedarsoft.WriteableObjectAccess;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A history contains several entries - each with a validation date
 */
public interface History<E extends HistoryEntry> extends WriteableObjectAccess<E> {
  @NotNull
  @NonNls
  String PROPERTY_ENTRIES = "entries";
  @NotNull
  @NonNls
  String PROPERTY_FIRST_ENTRIES = "firstEntry";

  /**
   * Returns all entries of the history
   *
   * @return the entries
   */
  @NotNull
  List<? extends E> getEntries();

  /**
   * Whether the history contains any entries
   */

  boolean hasEntries();

  /**
   * Adds an entry
   *
   * @param entry the entry that is added
   */
  void addEntry( @NotNull E entry );

  /**
   * Commits the entry
   *
   * @param entry the entry that has been changed
   */
  void commitEntry( @NotNull E entry );

  /**
   * Returns the latest entry
   *
   * @return the latest entry
   *
   * @throws NoValidElementFoundException
   */
  @NotNull
  E getLatestEntry() throws NoValidElementFoundException;

  /**
   * Registers an history listener
   *
   * @param historyListener the listener that is registered
   */
  void addHistoryListener( @NotNull HistoryListener<E> historyListener );

  @NotNull
  List<? extends HistoryListener<E>> getHistoryListeners();

  /**
   * Removes an history listener
   *
   * @param historyListener the listener that is removed
   */
  void removeHistoryListener( @NotNull HistoryListener<E> historyListener );

  /**
   * Returns whether the given entry is the latest entry
   *
   * @param entry the entry
   * @return whether the given entry is the latest entry
   */
  boolean isLatestEntry( @NotNull E entry );

  /**
   * Removes the entry
   *
   * @param entry the entry that is removed
   */
  boolean removeEntry( @NotNull E entry );

  /**
   * Returns the first entry
   *
   * @return the first entry
   *
   * @throws NoValidElementFoundException if no entry is available within this history
   */
  @NotNull
  E getFirstEntry() throws NoValidElementFoundException;

  /**
   * Removes all entries
   */
  void clear();
}