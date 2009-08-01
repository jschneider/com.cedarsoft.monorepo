package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.Tag;
import com.cedarsoft.utils.tags.TagProvider;
import com.cedarsoft.utils.tags.Taggable;
import com.cedarsoft.utils.tags.TagObservable;
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

  public void selectTag( @NotNull Tag tag ) {
    selectedTags.addTag( tag );
  }

  @NotNull
  public TagObservable getSelectedTags() {
    return selectedTags;
  }

  public void unselectTag( @NotNull Tag tag ) {
    if ( !selectedTags.removeTag( tag ) ) {
      throw new IllegalStateException( "Could not remove tag: " + tag );
    }
  }
}
