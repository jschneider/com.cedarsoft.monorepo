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

/**
 * <p>TagsComponent class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class TagsComponent extends JPanel {
  @NonNls
  @NotNull
  private static final String ACTION_KEY_ADD_TAG = "addTag";

  private TagsComponentModel model;
  @NonNls
  private static final String ACTION_REMOVE_SELECTED_TAG = "removeSelectedTag";

  /**
   * <p>Constructor for TagsComponent.</p>
   */
  public TagsComponent() {
    this( null );
  }

  /**
   * <p>Constructor for TagsComponent.</p>
   *
   * @param model a {@link TagsComponentModel} object.
   */
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
    editorComponent.getActionMap().put( ACTION_KEY_ADD_TAG, new AddSelectedAction() );

    //The list with the selected tags
    tagList.setCellRenderer( new TagListCellRenderer() );

    tagList.getInputMap().put( KeyStroke.getKeyStroke( "DELETE" ), ACTION_REMOVE_SELECTED_TAG );
    tagList.getActionMap().put( ACTION_REMOVE_SELECTED_TAG, new RemoveSelectedAction() );

    //The Button Actions
    addButton.addActionListener( new ActionListener() {
      @Override
      public void actionPerformed( ActionEvent e ) {
        addSelectedTag();
      }
    } );

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

  /**
   * <p>removeSelectedTag</p>
   */
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

  /**
   * <p>setAllowNewTagCreation</p>
   *
   * @param allowNewTagCreation a boolean.
   */
  public void setAllowNewTagCreation( boolean allowNewTagCreation ) {
    tagCombo.setEditable( allowNewTagCreation );
  }

  /**
   * <p>isAllowNewTagCreation</p>
   *
   * @return a boolean.
   */
  public boolean isAllowNewTagCreation() {
    return tagCombo.isEditable();
  }

  /**
   * <p>updateRemoveButtonState</p>
   */
  protected void updateRemoveButtonState() {
    removeButton.setEnabled( tagList.getSelectedIndices().length > 0 );
  }

  /**
   * <p>Getter for the field <code>model</code>.</p>
   *
   * @return a {@link TagsComponentModel} object.
   */
  @NotNull
  public TagsComponentModel getModel() {
    return model;
  }

  /**
   * <p>Getter for the field <code>tagCombo</code>.</p>
   *
   * @return a {@link JComboBox} object.
   */
  @NotNull
  public JComboBox getTagCombo() {
    return tagCombo;
  }

  /**
   * <p>Getter for the field <code>tagList</code>.</p>
   *
   * @return a {@link JList} object.
   */
  @NotNull
  public JList getTagList() {
    return tagList;
  }

  /**
   * <p>Setter for the field <code>model</code>.</p>
   *
   * @param model a {@link TagsComponentModel} object.
   */
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
  JButton addButton;
  private JScrollPane scrollPane1;
  private JList tagList;
  private JPanel panel1;
  protected JButton removeButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  /**
   * <p>Getter for the field <code>addButton</code>.</p>
   *
   * @return a {@link JButton} object.
   */
  @NotNull
  public JButton getAddButton() {
    return addButton;
  }

  /**
   * <p>Getter for the field <code>removeButton</code>.</p>
   *
   * @return a {@link JButton} object.
   */
  @NotNull
  public JButton getRemoveButton() {
    return removeButton;
  }

  private class AddSelectedAction extends AbstractAction {
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
  }

  private class RemoveSelectedAction extends AbstractAction {
    @Override
    public void actionPerformed( ActionEvent e ) {
      removeSelectedTag();
    }
  }
}
