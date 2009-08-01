package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Holds a list of entries
 */
public class SortedClusteredElementsCollection<E> extends ClusteredElementsCollection<E> {
  @Nullable
  private final Comparator<? super E> comparator;

  public SortedClusteredElementsCollection() {
    this( null );
  }

  public SortedClusteredElementsCollection( @Nullable Comparator<? super E> comparator ) {
    this.comparator = comparator;
  }

  @Override
  public void addElement( @NotNull E element ) {
    lock.writeLock().lock();
    int index;
    try {
      elements.add( element );
      if ( comparator == null ) {
        Collections.sort( ( List<Comparable> ) elements );
      } else {
        Collections.sort( elements, comparator );
      }
      index = elements.indexOf( element );
    } finally {
      lock.writeLock().unlock();
    }
    collectionSupport.elementAdded( element, index );
  }
}