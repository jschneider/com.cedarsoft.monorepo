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

import com.cedarsoft.swing.common.dialog.AbstractDialog;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.dialog.ButtonPanel;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Dialog for exceptions reporting progress
 */
public class ReportingExceptionsDialog extends AbstractDialog {
  public ReportingExceptionsDialog(@Nullable JFrame parent) {
    super(parent);
  }

  @Override
  public JComponent createBannerPanel() {
    BannerPanel bannerPanel = new BannerPanel(Messages.get("reporting.exception.title"), "");

    setTitle(Messages.get("reporting.exception.text"));

    return bannerPanel;
  }

  @Override
  public JComponent createContentPanel() {
    JPanel panel = new JPanel(new MigLayout("fillx, wrap 1", "grow, fill"));
    panel.setOpaque(false);
    JProgressBar progressBar = new JProgressBar();
    progressBar.setIndeterminate(true);
    progressBar.setString(Messages.get("reporting.exception.title"));
    panel.add(progressBar, "w 400");

    panel.add(new JLabel(Messages.get("reporting.exception.text")));

    return panel;
  }

  @Nullable
  @Override
  public ButtonPanel createButtonPanel() {
    return null;
  }
}
