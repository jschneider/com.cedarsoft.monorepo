package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.TagProvider;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;

/**
 *
 */
public abstract class AbstractTagsComponentModel implements TagsComponentModel {
  @NotNull
  protected final TagProvider tagProvider;

  protected AbstractTagsComponentModel( @NotNull TagProvider tagProvider ) {
    this.tagProvider = tagProvider;
  }

  @Override
  @NotNull
  public TagProvider getTagProvider() {
    return tagProvider;
  }
}
