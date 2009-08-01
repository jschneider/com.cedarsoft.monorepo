package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.Tag;
import com.cedarsoft.utils.tags.TagChangeListener;
import com.cedarsoft.utils.tags.TagChangeSupport;
import com.cedarsoft.utils.tags.TagObservable;
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
      public void valueChanged( ListSelectionEvent e ) {
        if ( e.getValueIsAdjusting() ) {
          return;
        }
        tagChangeSupport.notifyTagChanged( new TagChangeListener.TagChangeEvent( TagListSelectionMode.this, TagChangeListener.TagEventType.UNKNOWN, null, -1 ) );
      }
    } );
  }

  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.addTagChangeListener( listener );
  }

  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.removeTagChangeListener( listener );
  }

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
