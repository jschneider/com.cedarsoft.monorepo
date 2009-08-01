package com.cedarsoft.history;

import com.cedarsoft.Lockable;
import com.cedarsoft.NullLock;
import com.cedarsoft.PartTimeObjectAdd;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * An object access that is based on the current selection
 */
public class DelegatingObjectAccess<T> implements ObservableObjectAccess<T>, PartTimeObjectAdd<T> {
  @NotNull
  private final ElementsListener<T> delegatingListener = new ElementsListener<T>() {
    public void elementsDeleted( @NotNull ElementsChangedEvent<? extends T> event ) {
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsDeleted( event );
      }
    }

    public void elementsAdded( @NotNull ElementsChangedEvent<? extends T> event ) {
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsAdded( event );
      }
    }

    public void elementsChanged( @NotNull ElementsChangedEvent<? extends T> event ) {
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsChanged( event );
      }
    }
  };

  @NotNull
  private final List<ElementsListener<? super T>> listeners = new ArrayList<ElementsListener<? super T>>();

  @NotNull
  private final List<DelegateListener<T>> delegateListeners = new ArrayList<DelegateListener<T>>();

  @NotNull
  private final List<PartTimeListener> partTimeListeners = new ArrayList<PartTimeListener>();

  /**
   * Creates a new delegating object access
   *
   * @param currentDelegate the current  delegate
   */
  public DelegatingObjectAccess( @Nullable ObservableObjectAccess<T> currentDelegate ) {
    this.currentDelegate = currentDelegate;
  }

  /**
   * Returns the current delegate
   *
   * @return the current delegate
   */
  @NotNull
  protected ObservableObjectAccess<T> getCurrentDelegateSafe() {
    if ( currentDelegate == null ) {
      throw new IllegalStateException( "No current delegate available" );
    }
    return currentDelegate;
  }

  @Nullable
  public ObservableObjectAccess<T> getCurrentDelegate() {
    return currentDelegate;
  }

  @Nullable
  protected ObservableObjectAccess<T> currentDelegate;

  public boolean isCurrentDelegatingObjectAccessAvailable() {
    return currentDelegate != null;
  }

  /**
   * Sets the current delegate
   *
   * @param currentDelegate the current delegate
   */
  public void setCurrentDelegate( @Nullable ObservableObjectAccess<T> currentDelegate ) {
    //noinspection ObjectEquality
    if ( this.currentDelegate == currentDelegate ) {
      return;
    }

    ObservableObjectAccess<T> oldDelegate = this.currentDelegate;
    this.currentDelegate = currentDelegate;
    unregister( oldDelegate );
    register();

    //Notify the listeners
    for ( DelegateListener<T> delegateListener : delegateListeners ) {
      delegateListener.delegateChanged( currentDelegate );
    }

    if ( this.currentDelegate == null ) { //has changed to *not* available
      for ( PartTimeListener partTimeListener : partTimeListeners ) {
        partTimeListener.addNotAvailable();
      }
    } else if ( oldDelegate == null ) { //has changed to available
      for ( PartTimeListener partTimeListener : partTimeListeners ) {
        partTimeListener.addAvailable();
      }
    }
  }

  private void register() {
    ObservableObjectAccess<T> delegate = currentDelegate;
    if ( delegate != null ) {
      ReadWriteLock lock = getLock();

      lock.writeLock().lock();
      try {
        //Remove all meetings
        for ( ElementsListener<? super T> listener : listeners ) {
          listener.elementsAdded( new ElementsChangedEvent<T>( delegate, delegate.getElements(), 0, delegate.getElements().size() - 1 ) );
        }
        delegate.addElementListener( delegatingListener );
      } finally {
        lock.writeLock().unlock();
      }
    }
  }

  private void unregister( @Nullable ObservableObjectAccess<T> oldDelegate ) {
    if ( oldDelegate == null ) {
      return;
    }

    ReadWriteLock lock = getLock( oldDelegate );

    lock.writeLock().lock();
    try {
      //Notify the listeners about removal of all elements
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsDeleted( new ElementsChangedEvent<T>( oldDelegate, oldDelegate.getElements(), 0, oldDelegate.getElements().size() - 1 ) );
      }
      oldDelegate.removeElementListener( delegatingListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Returns the lock for the current delegate
   *
   * @return the lock for the current delegate
   */
  @NotNull
  public ReadWriteLock getLock() {
    ObservableObjectAccess<T> delegate = getCurrentDelegateSafe();
    return getLock( delegate );
  }

  @NotNull
  private ReadWriteLock getLock( @Nullable ObservableObjectAccess<T> delegate ) {
    if ( delegate != null && delegate instanceof Lockable ) {
      return ( ( Lockable ) delegate ).getLock();
    } else {
      return NullLock.LOCK;
    }
  }

  public void commit( @NotNull T element ) {
    getCurrentDelegateSafe().commit( element );
  }

  @NotNull
  public List<? extends T> getElements() {
    if ( isCurrentDelegatingObjectAccessAvailable() ) {
      return getCurrentDelegateSafe().getElements();
    } else {
      return Collections.emptyList();
    }
  }

  public void add( @NotNull T element ) {
    getCurrentDelegateSafe().add( element );
  }

  public void setElements( @NotNull List<? extends T> elements ) {
    getCurrentDelegateSafe().setElements( elements );
  }

  public void remove( @NotNull T element ) {
    getCurrentDelegateSafe().remove( element );
  }

  public void addElementListener( @NotNull ElementsListener<? super T> listener ) {
    this.listeners.add( listener );
  }

  public void removeElementListener( @NotNull ElementsListener<? super T> listener ) {
    this.listeners.remove( listener );
  }

  public void addDelegateListener( @NotNull DelegateListener<T> listener ) {
    this.delegateListeners.add( listener );
  }

  public void removeDelegateListener( @NotNull DelegateListener<T> listener ) {
    this.delegateListeners.remove( listener );
  }

  public boolean canAdd() {
    return isCurrentDelegatingObjectAccessAvailable();
  }

  public void addPartTimeListener( @NotNull PartTimeListener listener ) {
    partTimeListeners.add( listener );
  }

  public void removePartTimeListener( @NotNull PartTimeListener listener ) {
    partTimeListeners.remove( listener );
  }

  public interface DelegateListener<T> {
    /**
     * Is called if the delegate has changed
     *
     * @param newDelegate the new delegate
     */
    void delegateChanged( @Nullable ObservableObjectAccess<T> newDelegate );
  }
}
