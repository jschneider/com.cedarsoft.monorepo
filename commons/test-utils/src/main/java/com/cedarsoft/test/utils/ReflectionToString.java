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
package com.cedarsoft.test.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ReflectionToString {
  private ReflectionToString() {
  }

  @Nonnull
  public static String toString( @Nullable Object object ) {
    if ( object == null ) {
      return "<null>";
    }

    StringBuilder builder = new StringBuilder();
    builder.append( object.getClass().getName() ).append( " {\n" );

    Class<?> daClass = object.getClass();
    while ( daClass != null ) {
      for ( Field field : daClass.getDeclaredFields() ) {
        field.setAccessible( true );

        builder.append( "\t" );
        builder.append( field.getName() ).append( ": " );
        try {
          builder.append( field.get( object ) );
        } catch ( IllegalAccessException e ) {
          builder.append( "<failed due to " ).append( e.getMessage() ).append( ">" );
        }
        builder.append( "\n" );
      }
      daClass = daClass.getSuperclass();
    }

    builder.append( "}" );
    return builder.toString();
  }

}
