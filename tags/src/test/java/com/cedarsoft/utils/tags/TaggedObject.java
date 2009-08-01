package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Simple tagged object - used for testing purposes only.
 */
public class TaggedObject implements Taggable {
  private final TagSet tagSet = new TagSet( this );

  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.addTagChangeListener( listener );
  }

  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.removeTagChangeListener( listener );
  }

  public void setTags( @NotNull Tag... tags ) {
    tagSet.setTags( tags );
  }

  public boolean addTag( @NotNull Tag tag ) {
    return tagSet.addTag( tag );
  }

  public boolean removeTag( @NotNull Tag tag ) {
    return tagSet.removeTag( tag );
  }

  @NotNull
  public List<? extends Tag> getTags() {
    return tagSet.getTags();
  }
}
