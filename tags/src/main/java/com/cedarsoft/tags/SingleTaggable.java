package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * This implementation holds only *one* tag
 */
public class SingleTaggable implements Taggable {
  private final TagChangeSupport tcs;

  @Nullable
  private Tag selectedTag;

  public SingleTaggable( @NotNull Object source ) {
    tcs = new TagChangeSupport( source );
  }

  public void setSelectedTag( @Nullable Tag tag ) {
    if ( tag == null ) {
      if ( selectedTag != null ) {
        removeTag( selectedTag );
      }
    } else {
      addTag( tag );
    }
  }

  @Override
  public boolean addTag( @NotNull Tag tag ) {
    //noinspection ObjectEquality
    if ( tag == selectedTag ) {
      return false;
    }

    if ( selectedTag != null ) {
      removeTag( selectedTag );
    }

    this.selectedTag = tag;
    tcs.notifyTagAdded( tag, 0 );
    return true;
  }

  @Override
  public boolean removeTag( @NotNull Tag tag ) {
    //noinspection ObjectEquality
    if ( selectedTag != tag ) {
      throw new IllegalArgumentException( "Tag not found: " + tag );
    }

    selectedTag = null;
    tcs.notifyTagRemoved( tag, 0 );
    return true;
  }

  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    if ( selectedTag != null ) {
      return Collections.singletonList( selectedTag );
    } else {
      return Collections.emptyList();
    }
  }

  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tcs.addTagChangeListener( listener );
  }

  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tcs.removeTagChangeListener( listener );
  }

  @Nullable
  public Tag getSelectedTag() {
    return selectedTag;
  }
}
