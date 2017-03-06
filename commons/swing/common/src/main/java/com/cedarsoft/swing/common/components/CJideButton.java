package com.cedarsoft.swing.common.components;

import com.jidesoft.swing.JideButton;

import javax.swing.Action;
import javax.swing.Icon;
import java.awt.Cursor;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CJideButton extends JideButton {
  public CJideButton() {
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CJideButton(Icon icon) {
    super(icon);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CJideButton(String text) {
    super(text);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CJideButton(Action a) {
    super(a);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CJideButton(String text, Icon icon) {
    super(text, icon);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }
}
