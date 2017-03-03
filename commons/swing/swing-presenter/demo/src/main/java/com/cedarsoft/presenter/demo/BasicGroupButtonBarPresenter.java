/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.swing.presenter.demo;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.presenter.Presenter;
import com.cedarsoft.swing.presenter.SwingPresenter;
import javax.annotation.Nonnull;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.FlowLayout;

/**
 *
 */
public class BasicGroupButtonBarPresenter extends SwingPresenter<JPanel> {
  @Override
  @Nonnull
  protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
    BasicGroupButtonPresenter presenter = child.getLookup().lookup( BasicGroupButtonPresenter.class );
    if ( presenter != null ) {
      return presenter;
    }
    return new DefaultBasicButtonPresenter();
  }

  @Override
  protected void bind( @Nonnull JPanel presentation, @Nonnull StructPart struct, @Nonnull Lookup lookup ) {
  }

  @Override
  protected boolean shallAddChildren() {
    return true;
  }

  @Override
  @Nonnull
  protected JPanel createPresentation() {
    return new JPanel( new FlowLayout( FlowLayout.RIGHT, 0, 0 ) );
  }
}