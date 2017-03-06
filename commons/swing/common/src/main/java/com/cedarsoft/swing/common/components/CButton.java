package com.cedarsoft.swing.common.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Cursor;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CButton extends JButton {
  public CButton(Action a) {
    super(a);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CButton(String text, Icon icon) {
    super(text, icon);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CButton() {
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CButton(Icon icon) {
    super(icon);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  public CButton(String text) {
    super(text);
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }
}
