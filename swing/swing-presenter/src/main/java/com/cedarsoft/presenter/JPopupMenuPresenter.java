package com.cedarsoft.presenter;

import com.cedarsoft.commons.struct.StructPart;
import org.jetbrains.annotations.NotNull;

import javax.swing.JPopupMenu;
import java.lang.Override;

/**
 *
 */
public interface JPopupMenuPresenter extends Presenter<JPopupMenu> {
  @Override
  @NotNull
  JPopupMenu present( @NotNull StructPart struct );
}