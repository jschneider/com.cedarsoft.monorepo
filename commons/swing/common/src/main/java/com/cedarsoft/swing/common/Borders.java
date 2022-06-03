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

import javax.annotation.Nonnull;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

/**
 * Contains commonly used borders
 *
 */
public class Borders {
  public static final int TWO = UiScaler.scale(2);
  public static final int FIVE = UiScaler.scale(5);
  public static final int TEN = UiScaler.scale(10);
  public static final int FIFTEEN = UiScaler.scale(15);

  @Nonnull
  public static final Border DIALOG_CONTENT_BORDER = BorderFactory.createCompoundBorder(
    BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)
    , BorderFactory.createEmptyBorder(FIFTEEN, FIFTEEN, FIFTEEN, FIFTEEN)
  );

  @Nonnull
  public static final Border DIALOG_BUTTON_PANEL_BORDER = BorderFactory.createEmptyBorder(TEN, UiScaler.scale(100), TEN, TEN);
  @Nonnull
  public static final Border DEFAULT_15_PX = BorderFactory.createEmptyBorder(FIFTEEN, FIFTEEN, FIFTEEN, FIFTEEN);
  @Nonnull
  public static final Border DEFAULT_10_PX = BorderFactory.createEmptyBorder(TEN, TEN, TEN, TEN);
  @Nonnull
  public static final Border DEFAULT_5_PX = BorderFactory.createEmptyBorder(FIVE, FIVE, FIVE, FIVE);
  @Nonnull
  public static final Border LEFT_5_PX = BorderFactory.createEmptyBorder(0, FIVE, 0, 0);
  @Nonnull
  public static final Border LEFT_10_PX = BorderFactory.createEmptyBorder(0, FIVE, 0, 0);

  @Nonnull
  public static final Border TOP_2_PX = BorderFactory.createEmptyBorder(TWO, 0, 0, 0);
  @Nonnull
  public static final Border TOP_5_PX = BorderFactory.createEmptyBorder(FIVE, 0, 0, 0);
  @Nonnull
  public static final Border TOP_10_PX = BorderFactory.createEmptyBorder(TEN, 0, 0, 0);

  @Nonnull
  public static final Border ETCHED = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

  @Nonnull
  public static final Border MESSAGE_BORDER = BorderFactory.createCompoundBorder(
    BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY)
    , BorderFactory.createEmptyBorder(FIVE, FIVE, FIVE, FIVE)
  );

  @Deprecated
  @Nonnull
  public static final Border EMPTY_5_PX = DEFAULT_5_PX;


  private Borders() {
  }

}
