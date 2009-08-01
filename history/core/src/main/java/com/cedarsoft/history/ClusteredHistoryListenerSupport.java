package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Clustered history listener support
 */
public class ClusteredHistoryListenerSupport<E> {
  @NotNull
  private final HistoryListenerSupport<E> transientDelegate = new HistoryListenerSupport<E>();
  @NotNull
  private final NonTransientHistoryListenerSupport<E> nonTransientDelegate = new NonTransientHistoryListenerSupport<E>();


  public void addHistoryListener( @NotNull HistoryListener<E> historyListener, boolean isTransient ) {
    if ( isTransient ) {
      transientDelegate.addHistoryListener( historyListener );
    } else {
      nonTransientDelegate.addHistoryListener( historyListener );
    }
  }

  public void removeHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    transientDelegate.removeHistoryListener( historyListener );
    nonTransientDelegate.removeHistoryListener( historyListener );
  }

  public void notifyEntryChanged( @NotNull E entry ) {
    transientDelegate.notifyEntryChanged( entry );
    nonTransientDelegate.notifyEntryChanged( entry );
  }

  public void notifyEntryAdded( @NotNull E entry ) {
    transientDelegate.notifyEntryAdded( entry );
    nonTransientDelegate.notifyEntryAdded( entry );
  }

  public void notifyEntryRemoved( @NotNull E entry ) {
    transientDelegate.notifyEntryRemoved( entry );
    nonTransientDelegate.notifyEntryRemoved( entry );
  }

  @NotNull
  public List<? extends HistoryListener<E>> getTransientHistoryListeners() {
    return transientDelegate.getHistoryListeners();
  }
}
