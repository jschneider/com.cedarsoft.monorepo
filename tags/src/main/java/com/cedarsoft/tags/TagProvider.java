package com.cedarsoft.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * A tag provider provides available tags
 */
public interface TagProvider extends TagObservable {
  /**
   * Returns the tag for the given description
   *
   * @param description the description
   * @return the tag
   */
  @NotNull
  Tag getTag( @NotNull @NonNls String description );

  /**
   * Find the tag with the given description
   *
   * @param description the description
   * @return the tag
   *
   * @throws NotFoundException
   */
  @NotNull
  Tag findTag( @NonNls @NotNull String description ) throws NotFoundException;

  /**
   * Delets the tag with the given description
   *
   * @param description the description
   */
  void removeTag( @NotNull String description );

  /**
   * Removes the tag
   *
   * @param tag the tag that is removed
   */
  void removeTag( @NotNull Tag tag );
}