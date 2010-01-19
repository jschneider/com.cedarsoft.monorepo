/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.cmd;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that creates messages to be printed to the console.
 * Depending on the implementation the messages may be formated differently.
 */
public interface ConsolePrinter {
  /**
   * Creates an error message
   *
   * @param message the raw message
   * @param objects the objects
   * @return the error message
   */
  @NotNull
  String createError( @NotNull String message, @NotNull Object... objects );

  /**
   * Creates a warning
   *
   * @param message the raw message
   * @param objects the objects
   * @return the warning message
   */
  @NotNull
  String createWarning( @NotNull String message, @NotNull Object... objects );

  /**
   * Creates a success message
   *
   * @param message the raw message
   * @param objects the objects
   * @return the success mesage
   */
  @NotNull
  String createSuccess( @NotNull String message, @NotNull Object... objects );
}