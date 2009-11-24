package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Implementing classes have been availableTags.
 */
public interface Tagged {
  /**
   * Returns a list containing all tags this has been availableTags with
   *
   * @return a collection of the tags
   */
  @NotNull
  List<? extends Tag> getTags();
}
