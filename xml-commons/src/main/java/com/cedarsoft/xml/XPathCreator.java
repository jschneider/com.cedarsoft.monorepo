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

package com.cedarsoft.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Utility method that offers methods related to XPath
 */
public class XPathCreator {
  private XPathCreator() {
  }

  /**
   * Returns the path for a given element
   *
   * @param element the element
   * @return the path
   */
  @NotNull
  public static String createAbsolutePath( @NotNull Element element ) {
    StringBuilder builder = new StringBuilder();
    builder.append( element.getName() );


    Element actual = element;
    while ( true ) {
      Parent parent = actual.getParent();
      if ( parent == null ) {
        break;
      }

      if ( parent instanceof Element ) {
        //Add the index
        int index = calculateIndex( ( Element ) parent, actual ) + 1;//is 1 based
        if ( index != 1 ) {
          builder.insert( actual.getName().length(), "[" + index + ']' );
        }

        actual = ( Element ) parent;
        builder.insert( 0, '/' );
        builder.insert( 0, actual.getName() );
      } else if ( parent instanceof Document ) {
        builder.insert( 0, '/' );
        break;
      } else {
        throw new IllegalStateException( "uups " + parent );
      }
    }
    return builder.toString();
  }

  /**
   * Calculates the index of a child
   *
   * @param parent the parent
   * @param child  the child
   * @return the index
   */
  private static int calculateIndex( @NotNull Element parent, @NotNull Element child ) {
    if ( child.getParent() != parent ) {
      throw new IllegalArgumentException( "Wrong parent. Expected " + parent + " but was: " + child.getParent() );
    }

    List<?> children = parent.getChildren( child.getName() );
    return children.indexOf( child );
  }
}
