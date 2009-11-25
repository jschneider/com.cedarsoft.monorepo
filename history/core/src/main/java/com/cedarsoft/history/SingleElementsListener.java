package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

/**
 * Special element implementation that delegates the calls of {@link ElementsListener}
 * to methods with only *one* element as argument.
 *
 * @param <E> the type of the elements
 */
public abstract class SingleElementsListener<E> implements ElementsListener<E> {
  /**
   * Wraps the given element listener and creates an ElementsListener
   *
   * @param elementListener the wrapped element listener
   * @param <E>             the type
   * @return an ElementsListener that delegates to the given ElementListener
   */
  @NotNull
  public static <E> ElementsListener<E> wrap( @NotNull final ElementListener<E> elementListener ) {
    return new SingleElementsListener<E>() {
      @Override
      public void elementDeleted( @NotNull ObservableCollection<? extends E> source, @NotNull E element, int index ) {
        elementListener.elementDeleted( element );
      }

      @Override
      public void elementAdded( @NotNull ObservableCollection<? extends E> source, @NotNull E element, int index ) {
        elementListener.elementAdded( element );
      }

      @Override
      public void elementChanged( @NotNull ObservableCollection<? extends E> source, @NotNull E element, int index ) {
        elementListener.elementChanged( element );
      }
    };
  }

  /**
   * Is called when an entry has been deleted
   *
   * @param source
   * @param element the entry that has been deleted
   * @param index
   */
  public abstract void elementDeleted( @NotNull ObservableCollection<? extends E> source, @NotNull E element, int index );

  /**
   * Is called when an entry has been added
   *
   * @param source
   * @param element the entry that has been added
   * @param index
   */
  public abstract void elementAdded( @NotNull ObservableCollection<? extends E> source, @NotNull E element, int index );

  /**
   * Is called when an enty has been changed
   *
   * @param source
   * @param element the entry that has been changed
   * @param index
   */
  public abstract void elementChanged( @NotNull ObservableCollection<? extends E> source, @NotNull E element, int index );


  @Override
  public void elementsDeleted( @NotNull ElementsChangedEvent<? extends E> event ) {
    for ( int i = 0; i < event.getElements().size(); i++ ) {
      E e = event.getElements().get( i );
      int index = event.getIndicies().get( i );
      elementDeleted( event.getSource(), e, index );
    }
  }

  @Override
  public void elementsAdded( @NotNull ElementsChangedEvent<? extends E> event ) {
    for ( int i = 0; i < event.getElements().size(); i++ ) {
      E e = event.getElements().get( i );
      int index = event.getIndicies().get( i );
      elementAdded( event.getSource(), e, index );
    }
  }

  @Override
  public void elementsChanged( @NotNull ElementsChangedEvent<? extends E> event ) {
    for ( int i = 0; i < event.getElements().size(); i++ ) {
      E e = event.getElements().get( i );
      int index = event.getIndicies().get( i );
      elementChanged( event.getSource(), e, index );
    }
  }
}
