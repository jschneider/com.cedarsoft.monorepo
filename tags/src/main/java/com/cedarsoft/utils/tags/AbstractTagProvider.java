package com.cedarsoft.utils.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;

/**
 *
 */
public abstract class AbstractTagProvider implements TagProvider {
  @Override
  @NotNull
  public Tag getTag( @NotNull @NonNls String description ) {
    try {
      return findTag( description );
    } catch ( NotFoundException ignore ) {
      return createTag( description );
    }
  }

  @Override
  public void removeTag( @NotNull String description ) {
    Tag found = findTag( description );
    removeTag( found );
  }

  /**
   * Creates the tag for the given description
   *
   * @param description the description
   * @return the created tag
   */
  @NotNull
  public abstract Tag createTag( @NotNull @NonNls String description );
}

