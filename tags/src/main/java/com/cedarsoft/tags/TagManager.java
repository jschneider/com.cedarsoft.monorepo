/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.tags;

import com.cedarsoft.NotFoundException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * The TagManager offers methods to tag objects that do not directly implement {@link Taggable} oder
 * {@link Tagged}
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
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
   * @param <T> a T object.
   */
  void addTag( @NotNull T object, @NotNull String description );

  /**
   * Returns a {@link Taggable} for the given object
   *
   * @param o the object the taggable is searched for
   * @return the taggable - if one is found
   * @throws com.cedarsoft.NotFoundException if no taggable has been found
   */
  @NotNull
  Taggable findTaggable( @NotNull T o ) throws NotFoundException;

  /**
   * Returns a {@link Tagged} for the given object
   *
   * @param o the object the availableTags is searched for
   * @return the availableTags - if one is found
   * @throws com.cedarsoft.NotFoundException if no availableTags has been found
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
