package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EventListener;

/**
 * TagChangeListeners are notified when tags are changed
 */
public interface TagChangeListener extends EventListener {
  /**
   * A tag has been changed
   *
   * @param event the event
   */
  void tagChanged( @NotNull TagChangeEvent event );

  /**
   * Describes a tag change
   */
  class TagChangeEvent {
    @NotNull
    private final Object source;
    private final int index;
    @Nullable
    private final Tag tag;
    @NotNull
    private final TagEventType type;

    /**
     * Creates a new TagChangeEvent
     *
     * @param source the source
     * @param type   the type
     * @param tag    the tag
     * @param index  the index of the tag
     */
    public TagChangeEvent( @NotNull Object source, @NotNull TagEventType type, @Nullable Tag tag, int index ) {
      this.source = source;
      this.index = index;
      this.type = type;
      this.tag = tag;
    }

    @NotNull
    public Object getSource() {
      return source;
    }

    /**
     * The tag that has been changed
     *
     * @return the tag
     */
    @NotNull
    public Tag getTag() {
      if ( tag == null ) {
        throw new IllegalStateException( "No tag available" );
      }
      return tag;
    }

    /**
     * The type of change
     *
     * @return the type
     */
    @NotNull
    public TagEventType getType() {
      return type;
    }

    public int getIndex() {
      return index;
    }

    @Override
    public String toString() {
      return "TagChangeEvent{" +
        "source=" + source +
        ", index=" + index +
        ", tag=" + tag +
        ", type=" + type +
        '}';
    }
  }

  /**
   * Descripbes the type of an TagEvent
   */
  enum TagEventType {
    ADD, REMOVE, UNKNOWN
  }
}
