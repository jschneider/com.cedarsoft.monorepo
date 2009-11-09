package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A tag set contains several tags.
 */
public class TagSet implements Taggable {
  @NotNull
  @NonNls
  public static final String PROPERTY_TAGS = "tags";

  @NotNull
  private final TagChangeSupport tagChangeSupport = new TagChangeSupport();
  @NotNull
  private final List<Tag> tags = new ArrayList<Tag>();

  /**
   * ID field for persistence
   */
  @Deprecated
  private Long id;

  @Nullable
  private Object source;

  /**
   * Hibernate
   */
  @Deprecated
  protected TagSet() {
  }

  /**
   * Create a new TagSet
   *
   * @param source the source object
   */
  public TagSet( @Nullable Object source ) {
    this.source = source;
    if ( source == null ) {
      tagChangeSupport.setSource( this );
    } else {
      tagChangeSupport.setSource( source );
    }
  }

  /**
   * Sets the tags
   *
   * @param tags the tags
   */
  public void setTags( @NotNull Tag... tags ) {
    setTags( Arrays.asList( tags ) );
  }

  @Override
  public boolean addTag( @NotNull Tag tag ) {
    if ( tags.contains( tag ) ) {
      return false;
    } else {
      tags.add( tag );
      tagChangeSupport.notifyTagAdded( tag, tags.size() - 1 );
      return true;
    }
  }

  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    return Collections.unmodifiableList( tags );
  }

  /**
   * Sets the tags
   *
   * @param tags the tags
   */
  public void setTags( @NotNull List<? extends Tag> tags ) {
    List<Tag> newTags = new ArrayList<Tag>( tags );
    List<Tag> oldTags = new ArrayList<Tag>( this.tags );

    //Remove all tags that are no longer contained within the new tags
    for ( Tag oldTag : oldTags ) {
      if ( !newTags.remove( oldTag ) ) {
        removeTag( oldTag );
      }
    }

    for ( Tag newTag : newTags ) {
      addTag( newTag );
    }
  }

  @Override
  public boolean removeTag( @NotNull Tag tag ) {
    int index = tags.indexOf( tag );
    if ( index > -1 ) {
      tags.remove( index );
      tagChangeSupport.notifyTagRemoved( tag, index );
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.addTagChangeListener( listener );
  }

  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.removeTagChangeListener( listener );
  }

  @Nullable
  public Object getSource() {
    return source;
  }

  /**
   * Needed for hibernate
   *
   * @param source the source
   */
  private void setSource( @NotNull Object source ) {
    if ( this.source == source ) {
      return;
    }
    this.source = source;
    tagChangeSupport.setSource( source );
  }

  /**
   * XStream
   *
   * @return this
   */
  @NotNull
  private Object readResolve() {
    try {
      Field field = TagSet.class.getDeclaredField( "tagChangeSupport" );
      field.setAccessible( true );
      field.set( this, new TagChangeSupport( this ) );
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
    return this;
  }

  public void addAll( @NotNull List<? extends Tag> tags ) {
    for ( Tag tag : tags ) {
      addTag( tag );
    }
  }
}
