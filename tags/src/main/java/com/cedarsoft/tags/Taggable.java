package com.cedarsoft.tags;

import org.jetbrains.annotations.NotNull;

/**
 * Implementing classes may be availableTags
 */
public interface Taggable extends TagObservable {
  /**
   * Adds an additional tag
   *
   * @param tag the additional tag
   * @return true if the  tags collection changed as a result of the call
   */
  boolean addTag( @NotNull Tag tag );

  /**
   * Removes a tag
   *
   * @param tag the tag that is removed
   * @return true if the  tags collection changed as a result of the call
   */
  boolean removeTag( @NotNull Tag tag );
}