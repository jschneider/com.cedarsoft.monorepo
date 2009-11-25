package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.Tag;
import com.cedarsoft.tags.TagChangeListener;
import com.cedarsoft.tags.TagObservable;
import com.cedarsoft.tags.TagProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Date: May 3, 2007<br>
 * Time: 2:21:23 PM<br>
 */
public class TagListModel implements ListModel {
  protected final List<ListDataListener> listeners = new ArrayList<ListDataListener>();
  @NotNull
  protected final TagObservable availableTags;

  private boolean nullable;

  public TagListModel( @NotNull TagProvider availableTags ) {
    this( availableTags, false );
  }

  public TagListModel( @NotNull TagObservable availableTags, boolean nullable ) {
    this.availableTags = availableTags;
    this.nullable = nullable;
    availableTags.addTagChangeListener( new TagChangeListener() {
      @Override
      public void tagChanged( @NotNull TagChangeEvent event ) {
        notifyTagChanged( event );
      }
    } );
  }

  public boolean isNullable() {
    return nullable;
  }

  @Override
  public int getSize() {
    if ( nullable ) {
      return availableTags.getTags().size() + 1;
    } else {
      return availableTags.getTags().size();
    }
  }

  @Override
  @Nullable
  public Tag getElementAt( int index ) {
    if ( index < 0 ) {
      throw new IllegalArgumentException( "invalid index " + index );
    }

    if ( nullable ) {
      if ( index == 0 ) {
        return null;
      }
      return availableTags.getTags().get( index - 1 );
    } else {
      List<? extends Tag> tags = availableTags.getTags();
      if ( tags.size() <= index ) {
        return null;
      } else {
        return tags.get( index );
      }
    }
  }

  @Override
  public void addListDataListener( @NotNull ListDataListener l ) {
    listeners.add( l );
  }

  @Override
  public void removeListDataListener( @NotNull ListDataListener l ) {
    listeners.remove( l );
  }

  protected void notifyTagChanged( @NotNull TagChangeListener.TagChangeEvent event ) {
    if ( listeners.isEmpty() ) {
      return;
    }

    int type;
    int index0;
    int index1;
    switch ( event.getType() ) {
      case ADD:
        type = ListDataEvent.INTERVAL_ADDED;
        index0 = event.getIndex();
        index1 = event.getIndex();
        break;
      case REMOVE:
        type = ListDataEvent.INTERVAL_REMOVED;
        index0 = event.getIndex();
        index1 = event.getIndex();
        break;
      default:
        throw new IllegalStateException( "Uups" );
    }

    ListDataEvent listDataEvent = new ListDataEvent( this, type, index0, index1 );
    for ( ListDataListener listener : listeners ) {
      listener.contentsChanged( listDataEvent );
    }
  }

  @NotNull
  public TagObservable getAvailableTags() {
    return availableTags;
  }
}
