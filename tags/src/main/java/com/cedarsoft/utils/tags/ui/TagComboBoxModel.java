package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.SingleTaggable;
import com.cedarsoft.utils.tags.Tag;
import com.cedarsoft.utils.tags.TagObservable;
import com.cedarsoft.utils.tags.TagProvider;
import com.cedarsoft.utils.tags.Taggable;
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

  @Nullable
  public Tag getSelectedItem() {
    return taggable.getSelectedTag();
  }
}
