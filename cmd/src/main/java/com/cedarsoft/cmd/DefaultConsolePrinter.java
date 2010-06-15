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

package com.cedarsoft.cmd;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * <p/>
 * Date: 25.08.2006<br>
 * Time: 00:59:19<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class DefaultConsolePrinter implements ConsolePrinter {
  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String createError( @NotNull String message, @NotNull Object... objects ) {
    StringBuilder sb = new StringBuilder();
    String toPrint = MessageFormat.format( message, objects );
    for ( int i = 0; i < toPrint.length(); i++ ) {
      sb.append( "#" );
    }
    sb.append( toPrint );
    sb.append( "\n" );
    for ( int i = 0; i < toPrint.length(); i++ ) {
      sb.append( "#" );
    }

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String createWarning( @NotNull String message, @NotNull Object... objects ) {
    StringBuilder sb = new StringBuilder();
    String toPrint = MessageFormat.format( message, objects );

    sb.append( toPrint );
    sb.append( "\n" );
    for ( int i = 0; i < toPrint.length(); i++ ) {
      sb.append( "#" );
    }

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public String createSuccess( @NotNull String message, @NotNull Object... objects ) {
    StringBuilder sb = new StringBuilder();
    String toPrint = MessageFormat.format( message, objects );
    sb.append( "!!!" );
    sb.append( toPrint );

    return sb.toString();
  }
}
