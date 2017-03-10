package com.cedarsoft.swing.common.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ColoringTableCellRenderer implements TableCellRenderer {
  @Nullable
  private final TableCellRenderer delegate;

  @Nonnull
  private final ColorProvider colorProvider;

  /**
   * Creates a new coloring table cell renderer
   *
   * @param colorProvider the color provider
   */
  public ColoringTableCellRenderer( @Nonnull ColorProvider colorProvider ) {
    this( null, colorProvider );
  }

  /**
   * Creates a new coloring table cell renderer
   *
   * @param delegate      the optional delegate
   * @param colorProvider the color provider
   */
  public ColoringTableCellRenderer( @Nullable TableCellRenderer delegate, @Nonnull ColorProvider colorProvider ) {
    this.delegate = delegate;
    this.colorProvider = colorProvider;
  }

  @Override
  public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
    TableCellRenderer rendererToUse = findRendererToUse( table, column );

    Component component = rendererToUse.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

    Color background = colorProvider.getBackground(table, value, isSelected, hasFocus, row, column);
    if ( background != null ) {
      component.setBackground( background );
    } else {
      if ( isSelected ) {
        component.setBackground( table.getSelectionBackground() );
      } else {
        component.setBackground( table.getBackground() );
      }
    }

    Color foreground = colorProvider.getForeground(table, value, isSelected, hasFocus, row, column);
    if ( foreground != null ) {
      component.setForeground( foreground );
    } else {
      if ( isSelected ) {
        component.setForeground( table.getSelectionForeground() );
      } else {
        component.setForeground( table.getForeground() );
      }
    }

    return component;
  }

  @Nonnull
  private TableCellRenderer findRendererToUse( @Nonnull JTable table, int column ) {
    if ( delegate != null ) {
      return delegate;
    } else {
      return table.getDefaultRenderer( table.getColumnClass( column ) );
    }
  }

  public interface ColorProvider {
    @Nullable
    Color getBackground(@Nonnull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column);

    @Nullable
    Color getForeground(@Nonnull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column);
  }
}
