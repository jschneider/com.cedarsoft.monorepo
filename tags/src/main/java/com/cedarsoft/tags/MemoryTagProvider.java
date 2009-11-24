package com.cedarsoft.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.List;

/**
 * Holds the tags within a tag set
 */
public class MemoryTagProvider extends AbstractTagProvider {
  @NotNull
  protected final TagSet tags = new TagSet( this );

  @Override
  @NotNull
  public Tag createTag( @NotNull @NonNls String description ) {
    Tag newTag = new Tag( description );
    tags.addTag( newTag );
    return newTag;
  }

  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tags.addTagChangeListener( listener );
  }

  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tags.removeTagChangeListener( listener );
  }

  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    return tags.getTags();
  }

  @Override
  public void removeTag( @NotNull Tag tag ) {
    tags.removeTag( tag );
  }

  @Override
  @NotNull
  public Tag findTag( @NonNls @NotNull String description ) throws NotFoundException {
    for ( Tag tag : tags.getTags() ) {
      if ( tag.getDescription().equals( description ) ) {
        return tag;
      }
    }
    throw new NotFoundException( "No tag found for description <" + description + '>' );
  }
}
