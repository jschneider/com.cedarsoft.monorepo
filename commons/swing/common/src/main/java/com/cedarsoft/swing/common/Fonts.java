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
