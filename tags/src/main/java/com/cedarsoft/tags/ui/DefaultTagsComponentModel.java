package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.Tag;
import com.cedarsoft.tags.TagObservable;
import com.cedarsoft.tags.TagProvider;
import com.cedarsoft.tags.Taggable;
import org.jetbrains.annotations.NotNull;

/**
 * Dummy implementation
 */
public class DefaultTagsComponentModel extends AbstractTagsComponentModel {
  @NotNull
  private final Taggable selectedTags;

  public DefaultTagsComponentModel( @NotNull TagProvider tagProvider, @NotNull Taggable selectedTags ) {
    super( tagProvider );
    this.selectedTags = selectedTags;
  }

  @Override
  public void selectTag( @NotNull Tag tag ) {
    selectedTags.addTag( tag );
  }

  @Override
  @NotNull
  public TagObservable getSelectedTags() {
    return selectedTags;
  }

  @Override
  public void unselectTag( @NotNull Tag tag ) {
    if ( !selectedTags.removeTag( tag ) ) {
      throw new IllegalStateException( "Could not remove tag: " + tag );
    }
  }
}
