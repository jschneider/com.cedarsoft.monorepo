package com.cedarsoft.swing.common.components;

import javax.annotation.Nullable;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import java.awt.Cursor;

/**
 */
public class CToggleButton extends JToggleButton {

  public CToggleButton() {
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CToggleButton(@Nullable Action a) {
    super(a);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CToggleButton(@Nullable Icon icon) {
    super(icon);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CToggleButton(@Nullable String text) {
    super(text);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CToggleButton(@Nullable String text, boolean selected) {
    super(text, selected);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CToggleButton(@Nullable String text, @Nullable Icon icon) {
    super(text, icon);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CToggleButton(@Nullable String text, @Nullable Icon icon, boolean selected) {
    super(text, icon, selected);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

}
