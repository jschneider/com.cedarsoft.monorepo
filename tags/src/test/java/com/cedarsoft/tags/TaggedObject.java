package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Simple tagged object - used for testing purposes only.
 */
public class TaggedObject implements Taggable {
  private final TagSet tagSet = new TagSet( this );

  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.addTagChangeListener( listener );
  }

  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.removeTagChangeListener( listener );
  }

  public void setTags( @NotNull Tag... tags ) {
    tagSet.setTags( tags );
  }

  @Override
  public boolean addTag( @NotNull Tag tag ) {
    return tagSet.addTag( tag );
  }

  @Override
  public boolean removeTag( @NotNull Tag tag ) {
    return tagSet.removeTag( tag );
  }

  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    return tagSet.getTags();
  }
}
