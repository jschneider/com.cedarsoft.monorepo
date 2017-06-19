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
package com.cedarsoft.swing.common;

import com.cedarsoft.unit.other.pt;

import javax.annotation.Nonnull;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Commonly used fonts
 * TODO use {@link UiScaler}
 */
public interface Fonts {
  /**
   * Default label font
   */
  @Nonnull
  Font DEFAULT = create(Font.PLAIN, 0);

  @Nonnull
  Font TITLE = create(Font.PLAIN, 6.0f);

  @Nonnull
  Font SUB_TITLE = create(Font.BOLD, 2.0f);

  @Nonnull
  Font LARGER = create(Font.PLAIN, 4.0f);

  @Nonnull
  Font SMALLER = create(Font.PLAIN, -2.0f);
  @Nonnull
  Font BOLD = create(Font.BOLD, 0);
  @Nonnull
  Font BOLD_ITALIC = create(Font.BOLD | Font.ITALIC, 0);

  @Nonnull
  Font ITALIC = create(Font.ITALIC, 0);
  @Nonnull
  Font SIZE_10 = createWithSize(Font.PLAIN, 10);

  @Nonnull
  Font STRIKE_THROUGH = createStrikeThrough(DEFAULT);

  /**
   * Creates a font with the given style and size
   */
  @Nonnull
  static Font create(int style, @pt float relativeSize) {
    Font baseFont = getBaseFont();
    return baseFont.deriveFont(style, baseFont.getSize() + relativeSize);
  }

  /**
   * Creates a new font
   */
  @Nonnull
  static Font createWithSize(int style, @pt float size) {
    return getBaseFont().deriveFont(style, size);
  }

  @Nonnull
  static Font getBaseFont() {
    return new JLabel().getFont();
  }

  @Nonnull
  static Font createStrikeThrough(@Nonnull Font baseFont) {
    Map<AttributedCharacterIterator.Attribute, Object> attributes = new HashMap<AttributedCharacterIterator.Attribute, Object>();
    attributes.putAll(baseFont.getAttributes());
    attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);

    return new Font(attributes);
  }
}
