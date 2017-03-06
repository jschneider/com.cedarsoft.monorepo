package com.cedarsoft.swing.common.components;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import java.awt.Cursor;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CCheckbox extends JCheckBox {
  public CCheckbox() {
    setCursor();
  }

  public CCheckbox(Icon icon) {
    super(icon);
    setCursor();
  }

  public CCheckbox(Icon icon, boolean selected) {
    super(icon, selected);
    setCursor();
  }

  public CCheckbox(String text) {
    super(text);
    setCursor();
  }

  public CCheckbox(Action a) {
    super(a);
    setCursor();
  }

  public CCheckbox(String text, boolean selected) {
    super(text, selected);
    setCursor();
  }

  public CCheckbox(String text, Icon icon) {
    super(text, icon);
    setCursor();
  }

  public CCheckbox(String text, Icon icon, boolean selected) {
    super(text, icon, selected);
    setCursor();
  }

  private void setCursor() {
    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }
}
