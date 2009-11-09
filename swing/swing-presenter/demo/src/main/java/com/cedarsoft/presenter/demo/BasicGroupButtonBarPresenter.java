package com.cedarsoft.presenter.demo;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.presenter.Presenter;
import com.cedarsoft.presenter.SwingPresenter;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.lang.Override;

/**
 *
 */
public class BasicGroupButtonBarPresenter extends SwingPresenter<JPanel> {
  @Override
  @NotNull
  protected Presenter<? extends JComponent> getChildPresenter( @NotNull StructPart child ) {
    BasicGroupButtonPresenter presenter = child.getLookup().lookup( BasicGroupButtonPresenter.class );
    if ( presenter != null ) {
      return presenter;
    }
    return new DefaultBasicButtonPresenter();
  }

  @Override
  protected void bind( @NotNull JPanel presentation, @NotNull StructPart struct, @NotNull Lookup lookup ) {
  }

  @Override
  protected boolean shallAddChildren() {
    return true;
  }

  @Override
  @NotNull
  protected JPanel createPresentation() {
    return new JPanel( new FlowLayout( FlowLayout.RIGHT, 0, 0 ) );
  }
}