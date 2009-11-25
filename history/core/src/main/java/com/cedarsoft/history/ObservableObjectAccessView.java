package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A view on an ObservableObjectAccess that shows only some elements of the base object access...
 * This can be used to create an object access for only a sub type of the elements contains within an object access.
 */
public class ObservableObjectAccessView<E> implements ClusteredObservableObjectAccess<E> {
  @NotNull
  private final ClusteredObservableObjectAccess<? super E> base;

  @NotNull
  private final ClusteredElementsCollection<E> view = new ClusteredElementsCollection<E>();

  public ObservableObjectAccessView( @NotNull ClusteredObservableObjectAccess<? super E> base, @NotNull final Bridge<E> bridge ) {
    this.base = base;

    //fill with the initial values
    for ( Object element : base.getElements() ) {
      E bridged = bridge.getBridged( element );
      if ( bridged != null ) {
        view.add( bridged );
      }
    }

    this.base.addElementListener( new SingleElementsListener<Object>() {
      @Override
      public void elementDeleted( @NotNull ObservableCollection<? extends Object> source, @NotNull Object element, int index ) {
        E bridged = bridge.getBridged( element );
        if ( bridged != null ) {
          view.remove( bridged );
        }
      }

      @Override
      public void elementAdded( @NotNull ObservableCollection<? extends Object> source, @NotNull Object element, int index ) {
        E bridged = bridge.getBridged( element );
        if ( bridged != null ) {
          view.add( bridged );
        }
      }

      @Override
      public void elementChanged( @NotNull ObservableCollection<? extends Object> source, @NotNull Object element, int index ) {
        E bridged = bridge.getBridged( element );
        if ( bridged != null ) {
          view.commit( bridged );
        }
      }
    }, false );
  }

  @Override
  public void commit( @NotNull E element ) {
    base.commit( element );
  }

  @Override
  public void remove( @NotNull E element ) {
    base.remove( element );
  }

  @Override
  public void add( @NotNull E element ) {
    base.add( element );
  }

  @Override
  public void setElements( @NotNull List<? extends E> elements ) {
    base.setElements( elements );
  }

  @Override
  @NotNull
  public List<? extends E> getElements() {
    return view.getElements();
  }

  @Override
  public void addElementListener( @NotNull ElementsListener<? super E> listener ) {
    view.addElementListener( listener );
  }

  @Override
  public void addElementListener( @NotNull ElementsListener<? super E> listener, boolean isTransient ) {
    view.addElementListener( listener, isTransient );
  }

  @Override
  public void removeElementListener( @NotNull ElementsListener<? super E> listener ) {
    view.removeElementListener( listener );
  }

  @Override
  @NotNull
  public ReentrantReadWriteLock getLock() {
    return view.getLock();
  }

  @Override
  @NotNull
  public List<? extends ElementsListener<? super E>> getTransientElementListeners() {
    return view.getTransientElementListeners();
  }

  public interface Bridge<T> {
    /**
     * returns the bridged object (or null if there isn't a bridged object
     *
     * @param element the element
     * @return the bridged object or null
     */
    @Nullable
    T getBridged( @NotNull Object element );
  }

  public static class TypeBridge<T> implements Bridge<T> {
    @NotNull
    private final Class<T> type;

    public TypeBridge( @NotNull Class<T> type ) {
      this.type = type;
    }

    @Override
    @Nullable
    public T getBridged( @NotNull Object element ) {
      if ( type.isAssignableFrom( element.getClass() ) ) {
        return type.cast( element );
      } else {
        return null;
      }
    }
  }
}
