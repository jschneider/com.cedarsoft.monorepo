package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.TagProvider;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public abstract class AbstractTagsComponentModel implements TagsComponentModel {
  @NotNull
  protected final TagProvider tagProvider;

  protected AbstractTagsComponentModel( @NotNull TagProvider tagProvider ) {
    this.tagProvider = tagProvider;
  }

  @NotNull
  public TagProvider getTagProvider() {
    return tagProvider;
  }
}
