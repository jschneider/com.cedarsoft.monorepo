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
package com.cedarsoft.swing.common;

import com.cedarsoft.unit.other.px;
import com.jidesoft.swing.JideSplitPane;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.Graphics;

/**
 * Split pane that applies the divider location on the first paint
 */
public class CJideSplitPane extends JideSplitPane {
  public CJideSplitPane(int newOrientation) {
    super(newOrientation);
    setShowGripper(false);
    setContinuousLayout(true);
    setDividerSize(4);
  }

  @Nullable
  private int[] dividerLocationsToApply;

  private boolean hasBeenPainted;

  @Override
  public void setDividerLocations(@px @Nonnull int[] locations) {
    super.setDividerLocations(locations);

    if (!hasBeenPainted) {
      this.dividerLocationsToApply = locations.clone();
    }
  }

  @Override
  public void paint(Graphics g) {
    hasBeenPainted = true;
    if (dividerLocationsToApply != null) {
      setDividerLocations(dividerLocationsToApply);
      dividerLocationsToApply = null;
    }
    super.paint(g);
  }
}
