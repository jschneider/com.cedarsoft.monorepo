package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Simple tagged object - used for testing purposes only.
 */
public class TaggedObject implements Taggable {
  private final TagSet tagSet = new TagSet( this );

  @java.lang.Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.addTagChangeListener( listener );
  }

  @java.lang.Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.removeTagChangeListener( listener );
  }

  public void setTags( @NotNull Tag... tags ) {
    tagSet.setTags( tags );
  }

  @java.lang.Override
  public boolean addTag( @NotNull Tag tag ) {
    return tagSet.addTag( tag );
  }

  @java.lang.Override
  public boolean removeTag( @NotNull Tag tag ) {
    return tagSet.removeTag( tag );
  }

  @java.lang.Override
  @NotNull
  public List<? extends Tag> getTags() {
    return tagSet.getTags();
  }
}
