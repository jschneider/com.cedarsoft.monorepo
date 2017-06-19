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
package com.cedarsoft.exceptions.handling;

import com.cedarsoft.exceptions.ApplicationException;
import com.cedarsoft.swing.common.dialog.AbstractDialog;
import com.cedarsoft.swing.common.dialog.ComponentSizeStorage;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.swing.StyledLabel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Shows an application exception
 */
public class ApplicationExceptionDialog extends AbstractDialog {
  @Nonnull
  private final ApplicationException e;

  public ApplicationExceptionDialog(@Nullable JFrame parent, @Nonnull ApplicationException e, @Nullable ComponentSizeStorage.Backend preferences) {
    super(parent, preferences);
    this.e = e;
  }

  @Override
  public JComponent createBannerPanel() {
    BannerPanel bannerPanel = new BannerPanel(e.getErrorCode().toString(), e.getTitle());
    setTitle(e.getTitle() + " (" + e.getErrorCode() + ")");
    return bannerPanel;
  }

  @Override
  public JComponent createContentPanel() {
    JPanel panel = new JPanel(new BorderLayout()) {
      @Override
      public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = Math.max(preferredSize.width, 500);
        return preferredSize;
      }
    };

    StyledLabel styledLabel = new StyledLabel(e.getLocalizedMessage());
    panel.add(styledLabel);

    styledLabel.setVerticalAlignment(SwingConstants.TOP);
    styledLabel.setRows(5);
    styledLabel.setMaxRows(7);
    styledLabel.setLineWrap(true);

    return panel;
  }

}
