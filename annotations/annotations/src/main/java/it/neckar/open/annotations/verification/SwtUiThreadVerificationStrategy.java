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
package it.neckar.open.annotations.verification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

/**
 */
@Deprecated
public class SwtUiThreadVerificationStrategy implements UiThreadVerificationStrategy {
  @Nullable
  private static Method detectSwtGetCurrentMethod() {
    try {
      Class<?> display = Class.forName("org.eclipse.swt.widgets.Display");
      return display.getMethod("getCurrent");
    } catch (ClassNotFoundException ignore) {
    } catch (NoSuchMethodException ignore) {
    }
    return null;
  }

  @Nullable
  private final Method swtGetCurrentMethod;

  public SwtUiThreadVerificationStrategy() {
    this(detectSwtGetCurrentMethod());
  }

  public SwtUiThreadVerificationStrategy(@Nullable Method swtGetCurrentMethod) {
    this.swtGetCurrentMethod = swtGetCurrentMethod;
  }

  @Override
  public boolean isUiThread() {
    try {
      return swtGetCurrentMethod != null && swtGetCurrentMethod.invoke(null) != null;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e); //TODO remove exception(?)
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e); //TODO remove exception(?)
    }
  }
}
