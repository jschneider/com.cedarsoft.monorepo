package com.cedarsoft.utils.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * Abstract base class for {@link TagManager}
 */
public abstract class AbstractTagManager<T> implements TagManager<T> {
  @NotNull
  private final TagProvider tagProvider;

  /**
   * Creates a new abstract tag manager with the given factory
   *
   * @param tagProvider
   */
  protected AbstractTagManager( @NotNull TagProvider tagProvider ) {
    this.tagProvider = tagProvider;
  }

  @NotNull
  public Tag getTag( @NotNull @NonNls String description ) {
    return tagProvider.getTag( description );
  }

  @NotNull
  public List<? extends Tag> getTags() {
    return tagProvider.getTags();
  }

  @NotNull
  public Tag findTag( @NonNls @NotNull String description ) throws com.cedarsoft.NotFoundException {
    return tagProvider.findTag( description );
  }

  public void removeTag( @NotNull Tag tag ) {
    tagProvider.removeTag( tag );
  }

  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagProvider.addTagChangeListener( listener );
  }

  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagProvider.removeTagChangeListener( listener );
  }

  /**
   * Creates the taggable for the given object
   *
   * @param o th eobject
   * @return the taggable
   */
  @NotNull
  protected abstract Taggable createTaggable( @NotNull T o );

  @NotNull
  public Taggable getTaggable( @NotNull T o ) {
    try {
      return findTaggable( o );
    } catch ( NotFoundException ignore ) {
      return createTaggable( o );
    }
  }

  @NotNull
  public String getTagsAsString( @NotNull T o ) {
    List<? extends Tag> tags = getTaggable( o ).getTags();
    StringBuilder s = new StringBuilder();
    for ( Iterator<? extends Tag> it = tags.iterator(); it.hasNext(); ) {
      Tag tag = it.next();
      s.append( tag.getDescription() );
      if ( it.hasNext() ) {
        s.append( ", " );
      }
    }

    return s.toString();
  }

  public void addTag( @NotNull T object, @NotNull String description ) {
    getTaggable( object ).addTag( getTag( description ) );
  }

  public void removeTag( @NotNull T object, @NotNull String description ) {
    getTaggable( object ).removeTag( getTag( description ) );
  }

  /**
   * Remove the tag.
   * Handle with care!
   *
   * @param description the description
   */
  public void removeTag( @NonNls @NotNull String description ) throws NotFoundException {
    tagProvider.removeTag( description );
  }

  @NotNull
  public Tagged findTagged( @NotNull T o ) throws NotFoundException {
    return findTaggable( o );
  }
}
