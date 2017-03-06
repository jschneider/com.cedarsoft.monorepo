package com.cedarsoft.swing.common.components;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import java.awt.Component;
import java.awt.Cursor;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class CSpinner extends JSpinner {
  public CSpinner(SpinnerModel model) {
    super(model);
    setMouseCursor();
  }

  public CSpinner() {
    setMouseCursor();
  }

  private void setMouseCursor() {
    for (Component component : getComponents()) {
      if (component instanceof AbstractButton) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
    }
  }

  @Override
  public void setEditor(JComponent editor) {
    super.setEditor(editor);

    for (Component component : editor.getComponents()) {
      if (component instanceof AbstractButton) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
      }
    }
  }
}
