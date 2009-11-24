package com.cedarsoft.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * The TagManager offers methods to tag objects that do not directly implement {@link Taggable} oder
 * {@link Tagged}
 */
public interface TagManager<T> extends TagProvider {
  /**
   * Convenience method for:<br/>
   * <code>
   * getSelectionTaggable( object ).addTag( getTag( description ) );
   * </code>
   *
   * @param object      the object the taggable is availableTags for
   * @param description the description of the tag
   */
  void addTag( @NotNull T object, @NotNull String description );

  /**
   * Returns a {@link Taggable} for the given object
   *
   * @param o the object the taggable is searched for
   * @return the taggable - if one is found
   *
   * @throws NotFoundException if no taggable has been found
   */
  @NotNull
  Taggable findTaggable( @NotNull T o ) throws NotFoundException;

  /**
   * Returns a {@link Tagged} for the given object
   *
   * @param o the object the availableTags is searched for
   * @return the availableTags - if one is found
   *
   * @throws NotFoundException if no availableTags has been found
   */
  @NotNull
  Tagged findTagged( @NotNull T o ) throws NotFoundException;

  /**
   * Returns the taggable for the given object. If no taggable has been found, a new one should be created
   *
   * @param o the object
   * @return the taggable
   */
  @NotNull
  Taggable getTaggable( @NotNull T o );

  /**
   * Returns a string representation of all tags for the given object
   *
   * @param o the object
   * @return the string representation of all tags
   */
  @NotNull
  @NonNls
  String getTagsAsString( @NotNull T o );

  /**
   * Commit the changes for the given taggable.
   *
   * @param taggable the taggable
   */
  void commit( @NotNull Taggable taggable );

  /**
   * Remove the tag from the taggable of the given object.
   * This is a shortcut for <code>
   * getSelectionTaggable( object ).removeTag( getTag( description ) );
   * </code>
   *
   * @param object      the object
   * @param description the description
   */
  void removeTag( @NotNull T object, @NotNull String description );
}