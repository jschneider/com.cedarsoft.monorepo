package com.cedarsoft.utils.tags.ui;

import com.cedarsoft.utils.tags.Tag;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Component;

/**
 * <p/>
 * Date: Apr 3, 2007<br>
 * Time: 1:41:20 PM<br>
 */
public class TagListCellRenderer extends DefaultListCellRenderer {
  @Override
  public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
    super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
    if ( value instanceof Tag ) {
      setText( ( ( Tag ) value ).getDescription() );
    } else if ( value == null ) {
      setText( "*" );
    }
    return this;
  }
}
