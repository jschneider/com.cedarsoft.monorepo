package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Support class for TagChangeListeners
 */
public class TagChangeSupport {
  private final List<TagChangeListener> listeners = new ArrayList<TagChangeListener>();
  private Object source;

  /**
   * Attention! A source object must be set before this support can be used!
   * {@link #setSource(Object)}
   */
  public TagChangeSupport() {
  }

  /**
   * Creates a new TagChangeSupport for the given source
   *
   * @param source the source
   */
  public TagChangeSupport( @NotNull Object source ) {
    this.source = source;
  }

  /**
   * Sets the source. This source must be set if the default constructor has been used.
   *
   * @param source the source
   */
  public void setSource( @NotNull Object source ) {
    if ( this.source != null ) {
      throw new IllegalStateException( "The source has still been set!" );
    }
    this.source = source;
  }

  @NotNull
  public Object getSource() {
    if ( source == null ) {
      throw new IllegalStateException( "No source has been set" );
    }
    return source;
  }

  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    listeners.add( listener );
  }

  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    listeners.remove( listener );
  }

  /**
   * Notifies the listeners that a tag has been added
   *
   * @param tag   the tag
   * @param index the index
   */
  public void notifyTagAdded( @NotNull Tag tag, int index ) {
    notifyTagChanged( tag, TagChangeListener.TagEventType.ADD, index );
  }

  /**
   * Notifies the listeners that a tag has been removed
   *
   * @param tag   the tag
   * @param index the index
   */
  public void notifyTagRemoved( @NotNull Tag tag, int index ) {
    notifyTagChanged( tag, TagChangeListener.TagEventType.REMOVE, index );
  }

  /**
   * Notifies that listeners that a tag has been changed
   *
   * @param tag       the tag that has been chagned
   * @param eventType the event type
   * @param index     the index
   */
  public void notifyTagChanged( @NotNull Tag tag, @NotNull TagChangeListener.TagEventType eventType, int index ) {
    notifyTagChanged( new TagChangeListener.TagChangeEvent( getSource(), eventType, tag, index ) );
  }

  /**
   * notifies the listeners that a tag has been changed
   *
   * @param event the event
   */
  public void notifyTagChanged( @NotNull TagChangeListener.TagChangeEvent event ) {
    if ( !listeners.isEmpty() ) {
      for ( TagChangeListener listener : listeners ) {
        listener.tagChanged( event );
      }
    }
  }
}
