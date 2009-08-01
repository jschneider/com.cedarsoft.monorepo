package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.Tag;
import com.cedarsoft.utils.tags.TagProvider;
import com.cedarsoft.utils.tags.Taggable;
import com.cedarsoft.utils.tags.TagObservable;
import org.jetbrains.annotations.NotNull;

/**
 * The model for a {@link TagsComponent}.
 * Only the selected tags are observable!
 */
public interface TagsComponentModel {
  /**
   * Select the given tag
   *
   * @param tag the tag that is selected
   */
  void selectTag( @NotNull Tag tag );

  /**
   * unselect the tag
   *
   * @param tag the tag that is unselected
   */
  void unselectTag( @NotNull Tag tag );

  /**
   * Returns the selected tags
   *
   * @return the selected tags
   */
  @NotNull
  TagObservable getSelectedTags();

  /**
   * Returns the tag provider
   *
   * @return the tag provider
   */
  @NotNull
  TagProvider getTagProvider();
}
