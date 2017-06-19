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

import com.cedarsoft.annotations.UiThread;
import com.cedarsoft.unit.other.Scaled;
import com.cedarsoft.unit.other.ScalingFactor;
import com.cedarsoft.unit.other.Unscaled;

/**
 * Scales the UI
 */
@UiThread
public class UiScaler {
  /**
   * The scaling factor
   */
  @ScalingFactor
  private static double FACTOR = 1.0;

  private UiScaler() {
  }

  @UiThread
  public static void setFactor(@ScalingFactor double factor) {
    UiScaler.FACTOR = factor;
  }

  @UiThread
  @ScalingFactor
  public static double getFactor() {
    return FACTOR;
  }

  @Scaled
  public static int scale(@Unscaled int value) {
    return (int) Math.round(value * getFactor());
  }

  @Scaled
  public static int scaleFloor(@Unscaled int value) {
    return (int) Math.floor(value * getFactor());
  }

  @Scaled
  public static int scaleCeil(@Unscaled int value) {
    return (int) Math.ceil(value * getFactor());
  }

  @Unscaled
  public static int reverse(@Scaled int widthScaled) {
    return (int) Math.round(widthScaled / getFactor());
  }
}
