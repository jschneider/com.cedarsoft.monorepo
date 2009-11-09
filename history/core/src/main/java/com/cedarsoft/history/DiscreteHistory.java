package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDate;

import java.lang.Override;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * For several dates a hole history of values is stored.
 * This can be used to store a hole history of entries related to a given date.
 * <p/>
 * Be careful: Listeners that have ben registered to a sub history aren't notified of entries changes!
 */
public class DiscreteHistory<E extends DiscreteHistoryEntry> implements History<E> {
  @NotNull
  private final ClusteredHistoryListenerSupport<E> listenerSupport = new ClusteredHistoryListenerSupport<E>();

  private Long id;

  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @NotNull
  protected final SortedMap<LocalDate, DefaultHistory<E>> histories = new TreeMap<LocalDate, DefaultHistory<E>>();//todo make navigable

  /**
   * Returns the entries for the given date
   *
   * @param date the date the entries are searched for
   * @return the entries
   *
   * @throws HistoryNotFoundException if no history could be found for the given date
   */
  @NotNull
  public List<? extends E> getEntries( @NotNull LocalDate date ) throws HistoryNotFoundException {
    return getSubHistory( date ).getEntries();
  }

  /**
   * Returns the histories
   *
   * @return the histories
   */
  @NotNull
  Map<LocalDate, DefaultHistory<E>> getHistories() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      return Collections.unmodifiableMap( histories );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the latest entry for the given date
   *
   * @param date the date
   * @return the latest entry for the given date
   *
   * @throws HistoryNotFoundException if no history could be found for the given date
   */
  @NotNull
  public E getLatestEntry( @NotNull LocalDate date ) throws HistoryNotFoundException {
    return getSubHistory( date ).getLatestEntry();
  }

  /**
   * Returns the latest entry of each subhistory
   *
   * @return the latest entry of each subhistory
   */
  @NotNull
  public List<? extends E> getLatestEntries() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      List<E> latestEntries = new ArrayList<E>( histories.size() );

      for ( History<E> subHistory : histories.values() ) {
        latestEntries.add( subHistory.getLatestEntry() );
      }
      return latestEntries;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isLatestEntry( @NotNull E entry ) {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      for ( History<E> subHistory : histories.values() ) {
        if ( subHistory.isLatestEntry( entry ) ) {
          return true;
        }
      }
      return false;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns all entries
   *
   * @return all entries
   */
  @Override
  @NotNull
  public List<? extends E> getEntries() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      List<E> entries = new ArrayList<E>( histories.size() );

      for ( History<E> subHistory : histories.values() ) {
        entries.addAll( subHistory.getEntries() );
      }
      return entries;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Removes all entries
   */
  @Override
  public void clear() {
    lock.writeLock().lock();
    try {
      List<? extends E> entries = getEntries();
      for ( E entry : entries ) {
        removeEntry( entry );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Adds an entry.
   *
   * @param entry the entry
   */
  @Override
  public void addEntry( @NotNull E entry ) {
    getOrCreateSubHistory( entry.getValidityDate() ).addEntry( entry );
  }

  @Override
  public void commitEntry( @NotNull E entry ) {
    listenerSupport.notifyEntryChanged( entry );
  }

  @Override
  public boolean hasEntries() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        return false;
      }
      for ( DefaultHistory<E> history : histories.values() ) {
        if ( history.hasEntries() ) {
          return true;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return false;
  }

  @Override
  public boolean removeEntry( @NotNull E entry ) {
    ensureListenersRegistered();

    lock.writeLock().lock();
    try {
      for ( Iterator<DefaultHistory<E>> historyIterator = histories.values().iterator(); historyIterator.hasNext(); ) {
        DefaultHistory<E> history = historyIterator.next();
        if ( history.removeEntry( entry ) ) {
          //Remove history if empty
          if ( !history.hasEntries() ) {
            historyIterator.remove();
            HistoryListener<E> listener = getListeners().get( history );
            if ( listener != null ) {
              history.removeHistoryListener( listener );
            }
          }
          return true;
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
    return false;
  }

  @Override
  @NotNull
  public E getLatestEntry() throws NoValidElementFoundException {
    try {
      return getLastSubHistory().getLatestEntry();
    } catch ( HistoryNotFoundException e ) {
      throw new NoValidElementFoundException( e );
    }
  }

  @Override
  @NotNull
  public E getFirstEntry() throws NoValidElementFoundException {
    try {
      return getFirstSubHistory().getFirstEntry();
    } catch ( HistoryNotFoundException e ) {
      throw new NoValidElementFoundException( e );
    }
  }

  @NotNull
  private History<E> getOrCreateSubHistory( @NotNull LocalDate date ) {
    ensureListenersRegistered();

    lock.writeLock().lock();
    try {
      DefaultHistory<E> history = histories.get( date );
      if ( history == null ) {
        history = new DefaultHistory<E>();
        registerListener( history );
        histories.put( date, history );
      }
      return history;
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Registers the listener for the given history
   *
   * @param history the history the listener is registered for
   */
  private void registerListener( @NotNull DefaultHistory<E> history ) {
    lock.writeLock().lock();
    try {
      HistoryListener<E> listener = new HistoryListener<E>() {
        @Override
        public void entryAdded( @NotNull E entry ) {
          listenerSupport.notifyEntryAdded( entry );
        }

        @Override
        public void entryChanged( @NotNull E entry ) {
          listenerSupport.notifyEntryChanged( entry );
        }

        @Override
        public void entryRemoved( @NotNull E entry ) {
          listenerSupport.notifyEntryRemoved( entry );
        }
      };
      history.addHistoryListener( listener );
      getListeners().put( history, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Returns the sub history for the given date
   *
   * @param date the date the sub history is searched for
   * @return the sub history for the given date
   */
  @NotNull
  public History<E> getSubHistory( @NotNull LocalDate date ) throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      History<E> subHistory = histories.get( date );
      if ( subHistory == null ) {
        throw new HistoryNotFoundException( "No sub history found for " + date );
      }
      return subHistory;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the sub history before the given date
   *
   * @param date the date
   * @return the sub history before the given date
   */
  @NotNull
  public History<E> getSubHistoryBefore( @NotNull LocalDate date ) throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        throw new HistoryNotFoundException( "No histories are available yet" );
      }

      //Iterating backwards would be much easier
      Map.Entry<LocalDate, DefaultHistory<E>> currentlyBest = null;
      for ( Map.Entry<LocalDate, DefaultHistory<E>> entry : histories.entrySet() ) {
        //if we are no longer before --> return the best value
        if ( !entry.getKey().isBefore( date ) ) {
          break;
        }

        if ( currentlyBest == null || currentlyBest.getKey().isBefore( entry.getKey() ) ) {
          currentlyBest = entry;
        }
      }

      //Now return the best value
      if ( currentlyBest == null ) {
        throw new HistoryNotFoundException( "No history found before " + date );
      } else {
        DefaultHistory<E> value = currentlyBest.getValue();
        if ( value == null ) {
          throw new IllegalStateException( "value is null for " + currentlyBest.getKey() );
        }
        return value;
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the best sub history for the given date.
   * This method returns the sub history for the given date (if there is one) or the
   * closest history *before* the given date
   *
   * @param date the date
   * @return the best history
   *
   * @throws HistoryNotFoundException if absolutly no history could be found
   */
  @NotNull
  public History<E> getBestSubHistoryFor( @NotNull LocalDate date ) throws HistoryNotFoundException {
    //Check for direct hit
    try {
      return getSubHistory( date );
    } catch ( HistoryNotFoundException ignore ) {
    }
    return getSubHistoryBefore( date );
  }

  /**
   * Returns the most up to date entry (over all histories)
   *
   * @return the most up to date entry (over all histories)
   */
  @NotNull
  public E getMostUpToDateEntry() throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    E upToDate = null;
    try {
      for ( History<E> history : histories.values() ) {
        for ( E entry : history.getEntries() ) {
          if ( upToDate == null || upToDate.getVerificationDate().isBefore( entry.getVerificationDate() ) ) {
            upToDate = entry;
          }
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    if ( upToDate == null ) {
      throw new HistoryNotFoundException( "No cash value informations available" );
    }
    return upToDate;
  }

  @NotNull
  public E getBestEntryFor( @NotNull LocalDate date ) throws HistoryNotFoundException {
    return getBestSubHistoryFor( date ).getLatestEntry();
  }

  @NotNull
  public History<E> getLastSubHistory() throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        throw new HistoryNotFoundException( "No Subhistories available" );
      }
      return histories.get( histories.lastKey() );
    } finally {
      lock.readLock().unlock();
    }
  }

  @NotNull
  public History<E> getFirstSubHistory() throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        throw new HistoryNotFoundException( "No Subhistories available" );
      }
      return histories.get( histories.firstKey() );
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  @NotNull
  public List<? extends E> getElements() {
    return getEntries();
  }

  @Override
  public void setElements( @NotNull List<? extends E> elements ) {
    throw new UnsupportedOperationException( "Cannot set the elements yet" ); //todo implement
  }

  @Override
  public void add( @NotNull E element ) {
    addEntry( element );
  }

  @Override
  public void remove( @NotNull E element ) {
    removeEntry( element );
  }

  @Override
  public void removeHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    listenerSupport.removeHistoryListener( historyListener );
  }

  @Override
  public void addHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    listenerSupport.addHistoryListener( historyListener, true );
  }

  public void addHistoryListener( @NotNull HistoryListener<E> historyListener, boolean isTransient ) {
    listenerSupport.addHistoryListener( historyListener, isTransient );
  }

  @Override
  @NotNull
  public List<? extends HistoryListener<E>> getHistoryListeners() {
    return listenerSupport.getTransientHistoryListeners();
  }

  private transient Map<History<E>, HistoryListener<E>> listeners;

  @NotNull
  private Map<History<E>, HistoryListener<E>> getListeners() {
    //First try with read access
    lock.readLock().lock();
    try {
      if ( listeners != null ) {
        return listeners;
      }
    } finally {
      lock.readLock().unlock();
    }

    //Now with write access
    lock.writeLock().lock();
    try {
      if ( listeners == null ) {
        listeners = new WeakHashMap<History<E>, HistoryListener<E>>();

        //Now create the listeners for the existing histories
        for ( DefaultHistory<E> history : histories.values() ) {
          this.registerListener( history );
        }
      }
      //noinspection ReturnOfCollectionOrArrayField
      return listeners;
    } finally {
      lock.writeLock().unlock();
    }
  }

  @NotNull
  public ReadWriteLock getLock() {
    return lock;
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

  /**
   * This method ensures that the listeners are initialized properly.
   * This is necessary to update the listeners on deserialized objects.
   */
  private void ensureListenersRegistered() {
    getListeners();
  }
}
