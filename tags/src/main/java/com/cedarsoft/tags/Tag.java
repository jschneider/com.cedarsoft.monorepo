package com.cedarsoft.tags;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * A Tag
 */
public class Tag {
  @NotNull
  @NonNls
  public static final String PROPERTY_DESCRIPTION = "description";

  @NonNls
  private final String description;

  /**
   * Only for persistence
   */
  private Long id;

  /**
   * Hibernate Constructor
   */
  @Deprecated
  protected Tag() {
    description = null;
  }

  /**
   * Creates a new tag with the given description.
   * Tags should not be instaniated directly. Use the {@link TagManager} instead.
   *
   * @param description the description
   */
  public Tag( @NonNls @NotNull String description ) {
    this.description = description;
  }

  /**
   * Returns the description
   *
   * @return the description
   */
  @NotNull
  @NonNls
  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return getDescription();
  }
}
