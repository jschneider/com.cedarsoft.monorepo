package com.cedarsoft.presenter;

import org.jetbrains.annotations.NotNull;

import javax.swing.JCheckBox;
import java.lang.Override;

/**
 * Creates a checkbox.
 */
public class JCheckboxPresenter extends AbstractButtonPresenter<JCheckBox> {
  @Override
  protected boolean shallAddChildren() {
    return false;
  }

  @Override
  @NotNull
  protected JCheckBox createPresentation() {
    return new JCheckBox();
  }
}
