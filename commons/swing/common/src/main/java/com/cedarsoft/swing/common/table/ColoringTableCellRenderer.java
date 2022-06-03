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
package com.cedarsoft.swing.common.table;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

/**
 */
public class ColoringTableCellRenderer implements TableCellRenderer {
  @Nullable
  private final TableCellRenderer delegate;

  @Nonnull
  private final ColorProvider colorProvider;
  @Nullable
  private final FontProvider fontProvider;

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
  public ColoringTableCellRenderer(@Nullable TableCellRenderer delegate, @Nonnull ColorProvider colorProvider) {
    this(delegate, colorProvider, null);
  }

  public ColoringTableCellRenderer(@Nullable TableCellRenderer delegate, @Nonnull ColorProvider colorProvider, @Nullable FontProvider fontProvider) {
    this.delegate = delegate;
    this.colorProvider = colorProvider;
    this.fontProvider = fontProvider;
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

    if (fontProvider != null) {
      @Nullable Font font = fontProvider.getFont(table, value, isSelected, hasFocus, row, column);
      component.setFont(font);
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

  public interface FontProvider {
    @Nullable
    Font getFont(@Nonnull JTable table, @Nullable Object value, boolean isSelected, boolean hasFocus, int row, int column);
  }
}
