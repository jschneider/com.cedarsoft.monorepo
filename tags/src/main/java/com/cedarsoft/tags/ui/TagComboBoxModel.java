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

package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.SingleTaggable;
import com.cedarsoft.tags.Tag;
import com.cedarsoft.tags.TagObservable;
import com.cedarsoft.tags.TagProvider;
import com.cedarsoft.tags.Taggable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * ComboboxModel prsenting tags
 */
public class TagComboBoxModel extends TagListModel implements ComboBoxModel {
  @NotNull
  private final SingleTaggable taggable = new SingleTaggable( this );

  public TagComboBoxModel( @NotNull TagObservable availableTags ) {
    this( availableTags, true );
  }

  public TagComboBoxModel( @NotNull TagObservable availableTags, boolean nullable ) {
    super( availableTags, nullable );
  }

  /**
   * Returns a taggable representing the selection
   *
   * @return the selection
   */
  @NotNull
  public Taggable getSelectionTaggable() {
    return taggable;
  }

  @Override
  public void setSelectedItem( @Nullable Object anItem ) {
    //noinspection ObjectEquality
    if ( anItem == getSelectedItem() ) {
      return;
    }

    @Nullable
    final Tag newTag;

    if ( anItem == null || anItem instanceof Tag ) {
      newTag = ( Tag ) anItem;
    } else if ( anItem instanceof String ) {
      if ( ( ( CharSequence ) anItem ).length() == 0 ) {
        newTag = null;
      } else {
        newTag = getTagProvider().getTag( ( String ) anItem );
      }
    } else {
      throw new IllegalStateException( "???? " + anItem.getClass().getName() );
    }

    taggable.setSelectedTag( newTag );

    if ( !listeners.isEmpty() ) {
      ListDataEvent event = new ListDataEvent( this, ListDataEvent.CONTENTS_CHANGED, -1, -1 );
      for ( ListDataListener listener : listeners ) {
        listener.contentsChanged( event );
      }
    }
  }

  @NotNull
  private TagProvider getTagProvider() {
    if ( availableTags instanceof TagProvider ) {
      return ( TagProvider ) availableTags;
    } else {
      throw new IllegalStateException( "Can't create tags - need a tag provider" );
    }
  }

  @Override
  @Nullable
  public Tag getSelectedItem() {
    return taggable.getSelectedTag();
  }
}
