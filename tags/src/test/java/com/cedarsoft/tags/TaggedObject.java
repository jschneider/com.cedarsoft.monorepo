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

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Simple tagged object - used for testing purposes only.
 */
public class TaggedObject implements Taggable {
  private final TagSet tagSet = new TagSet( this );

  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.addTagChangeListener( listener );
  }

  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagSet.removeTagChangeListener( listener );
  }

  public void setTags( @NotNull Tag... tags ) {
    tagSet.setTags( tags );
  }

  @Override
  public boolean addTag( @NotNull Tag tag ) {
    return tagSet.addTag( tag );
  }

  @Override
  public boolean removeTag( @NotNull Tag tag ) {
    return tagSet.removeTag( tag );
  }

  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    return tagSet.getTags();
  }
}
