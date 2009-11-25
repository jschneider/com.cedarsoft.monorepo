package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.Tag;
import com.cedarsoft.tags.TagObservable;
import com.cedarsoft.tags.TagProvider;
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
