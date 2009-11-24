/*
 * Created by JFormDesigner on Tue Apr 03 13:02:00 CEST 2007
 */

package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.DefaultTagManager;
import com.cedarsoft.tags.MemoryTagManager;
import com.cedarsoft.tags.Tag;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Override;

/**
 */
public class TagsComponent extends JPanel {
  @NonNls
  @NotNull
  private static final String ACTION_KEY_ADD_TAG = "addTag";

  private TagsComponentModel model;
  @NonNls
  private static final String ACTION_REMOVE_SELECTED_TAG = "removeSelectedTag";

  public TagsComponent() {
    this( null );
  }

  public TagsComponent( @Nullable TagsComponentModel model ) {
    initComponents();

    //Set the model
    if ( model != null ) {
      setModel( model );
    } else {
      MemoryTagManager<Object> tagManager = new DefaultTagManager<Object>();
      setModel( new DefaultTagsComponentModel( tagManager, tagManager.getTaggable( this ) ) );
    }

    //Initialize the combo box
    tagCombo.setRenderer( new TagListCellRenderer() );
    JComponent editorComponent = ( JComponent ) tagCombo.getEditor().getEditorComponent();

    editorComponent.getInputMap().put( KeyStroke.getKeyStroke( "ENTER" ), ACTION_KEY_ADD_TAG );
    editorComponent.getActionMap().put( ACTION_KEY_ADD_TAG, new AbstractAction() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        tagCombo.setSelectedItem( tagCombo.getEditor().getItem() );
        if ( tagCombo.isPopupVisible() ) {
          tagCombo.setPopupVisible( false );
        } else {
          addSelectedTag();
          tagCombo.getEditor().selectAll();
        }
      }
    } );

    //The list with the selected tags
    tagList.setCellRenderer( new TagListCellRenderer() );

    tagList.getInputMap().put( KeyStroke.getKeyStroke( "DELETE" ), ACTION_REMOVE_SELECTED_TAG );
    tagList.getActionMap().put( ACTION_REMOVE_SELECTED_TAG, new AbstractAction() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        removeSelectedTag();
      }
    } );

    //The Button Actions
    ActionListener addListener = new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        addSelectedTag();
      }
    };
    addButton.addActionListener( addListener );

    removeButton.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        removeSelectedTag();
      }
    } );
  }

  private void addSelectedTag() {
    Tag tag = ( Tag ) tagCombo.getSelectedItem();
    if ( tag != null ) {
      this.model.selectTag( tag );
    }
  }

  protected void removeSelectedTag() {
    Object[] selected = tagList.getSelectedValues();
    if ( selected == null ) {
      return;
    }

    for ( Object o : selected ) {
      if ( o != null ) {
        this.model.unselectTag( ( Tag ) o );
      }
    }
  }

  public void setAllowNewTagCreation( boolean allowNewTagCreation ) {
    tagCombo.setEditable( allowNewTagCreation );
  }

  public boolean isAllowNewTagCreation() {
    return tagCombo.isEditable();
  }

  protected void updateRemoveButtonState() {
    removeButton.setEnabled( tagList.getSelectedIndices().length > 0 );
  }

  @NotNull
  public TagsComponentModel getModel() {
    return model;
  }

  @NotNull
  public JComboBox getTagCombo() {
    return tagCombo;
  }

  @NotNull
  public JList getTagList() {
    return tagList;
  }

  public final void setModel( @NotNull TagsComponentModel model ) {
    this.model = model;
    tagCombo.setModel( new TagComboBoxModel( getModel().getTagProvider(), false ) );
    TagListModel tagListModel = new TagListModel( getModel().getSelectedTags(), false );
    tagList.setModel( tagListModel );

    tagList.setSelectionModel( new TagListSelectionMode( tagListModel ) );
    tagList.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
      @Override
      public void valueChanged( ListSelectionEvent e ) {
        if ( e.getValueIsAdjusting() ) {
          return;
        }
        updateRemoveButtonState();
      }
    } );
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    tagCombo = new JComboBox();
    addButton = new JButton();
    scrollPane1 = new JScrollPane();
    tagList = new JList();
    panel1 = new JPanel();
    removeButton = new JButton();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setLayout( new FormLayout(
      new ColumnSpec[]{
        new ColumnSpec( ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW ),
        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
        new ColumnSpec( ColumnSpec.FILL, Sizes.PREFERRED, FormSpec.NO_GROW ),
      },
      new RowSpec[]{
        new RowSpec( RowSpec.FILL, Sizes.DEFAULT, FormSpec.NO_GROW ),
        FormFactory.LINE_GAP_ROWSPEC,
        new RowSpec( RowSpec.FILL, Sizes.dluY( 60 ), FormSpec.DEFAULT_GROW )
      } ) );

    //---- tagCombo ----
    tagCombo.setEditable( true );
    add( tagCombo, cc.xy( 1, 1 ) );

    //---- addButton ----
    addButton.setText( "+" );
    add( addButton, cc.xy( 3, 1 ) );

    //======== scrollPane1 ========
    {
      scrollPane1.setViewportView( tagList );
    }
    add( scrollPane1, cc.xy( 1, 3 ) );

    //======== panel1 ========
    {
      panel1.setLayout( new FormLayout(
        new ColumnSpec[]{
          new ColumnSpec( ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW )
        },
        new RowSpec[]{
          new RowSpec( RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW )
        }
      ) );

      //---- removeButton ----
      removeButton.setText( "-" );
      removeButton.setEnabled( false );
      panel1.add( removeButton, cc.xy( 1, 1 ) );
    }
    add( panel1, cc.xy( 3, 3 ) );
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  JComboBox tagCombo;
  private JButton addButton;
  private JScrollPane scrollPane1;
  private JList tagList;
  private JPanel panel1;
  private JButton removeButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  @NotNull
  public JButton getAddButton() {
    return addButton;
  }

  @NotNull
  public JButton getRemoveButton() {
    return removeButton;
  }

}
