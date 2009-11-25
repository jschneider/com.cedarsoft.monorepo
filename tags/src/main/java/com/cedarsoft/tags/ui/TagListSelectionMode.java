package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.Tag;
import com.cedarsoft.tags.TagChangeListener;
import com.cedarsoft.tags.TagChangeSupport;
import com.cedarsoft.tags.TagObservable;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class TagListSelectionMode extends DefaultListSelectionModel implements TagObservable {
  private final transient TagChangeSupport tagChangeSupport = new TagChangeSupport();
  private final transient TagListModel model;

  public TagListSelectionMode( @NotNull TagListModel model ) {
    this.model = model;
    addListSelectionListener( new ListSelectionListener() {
      @Override
      public void valueChanged( ListSelectionEvent e ) {
        if ( e.getValueIsAdjusting() ) {
          return;
        }
        tagChangeSupport.notifyTagChanged( new TagChangeListener.TagChangeEvent( TagListSelectionMode.this, TagChangeListener.TagEventType.UNKNOWN, null, -1 ) );
      }
    } );
  }

  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.addTagChangeListener( listener );
  }

  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.removeTagChangeListener( listener );
  }

  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    int min = getMinSelectionIndex();
    int max = getMaxSelectionIndex();

    if ( min < max ) {
      return Collections.emptyList();
    }

    List<Tag> tags = new ArrayList<Tag>();
    for ( int i = min; i <= max; i++ ) {
      if ( isSelectedIndex( i ) ) {
        tags.add( model.getElementAt( i ) );
      }
    }
    return Collections.unmodifiableList( tags );
  }
}
