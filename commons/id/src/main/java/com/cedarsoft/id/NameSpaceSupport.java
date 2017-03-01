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

package com.cedarsoft.id;


import javax.annotation.Nonnull;

/**
 *
 */
public class NameSpaceSupport {
  @Nonnull
  public static String createNameSpaceUriBase( @Nonnull Class<?> type ) {
    return createNameSpaceUriBase( type.getName() );
  }

  @Nonnull
  public static String createNameSpaceUriBase( @Nonnull String className ) {
    String[] parts = className.split( "\\." );

    //If we have lesser than three parts just return the type - a fallback
    if ( parts.length < 3 ) {
      return "http://" + className;
    }

    StringBuilder uri = new StringBuilder( "http://" );
    uri.append( parts[1] );
    uri.append( "." );
    uri.append( parts[0] );

    for ( int i = 2, partsLength = parts.length; i < partsLength; i++ ) {
      String part = parts[i];
      uri.append( "/" );

      uri.append( createNameWithSpaces( part ) );
    }

    return uri.toString();
  }

  @Nonnull
  public static String createNameWithSpaces( @Nonnull String camelName ) {
    //If it is the same, just return it
    String lowerCaseName = camelName.toLowerCase();
    if ( lowerCaseName.equals( camelName ) ) {
      return camelName;
    }

    StringBuilder builder = new StringBuilder();
    for ( int i = 0; i < camelName.length(); i++ ) {
      @Nonnull
      String camelPart = camelName.substring( i, i + 1 );
      String asLower = camelPart.toLowerCase();
      if ( builder.length() > 0 && !asLower.equals( camelPart ) ) {
        builder.append( "-" );
      }
      builder.append( asLower );
    }

    return builder.toString();
  }
}
