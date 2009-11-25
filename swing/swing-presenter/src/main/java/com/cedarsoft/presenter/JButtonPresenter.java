package com.cedarsoft.presenter;

import org.jetbrains.annotations.NotNull;

import javax.swing.JButton;

/**
 * Creates a button
 */
public class JButtonPresenter extends AbstractButtonPresenter<JButton> {
  @Override
  @NotNull
  public JButton createPresentation() {
    return new JButton();
  }

  @Override
  protected boolean shallAddChildren() {
    return false;
  }
}
