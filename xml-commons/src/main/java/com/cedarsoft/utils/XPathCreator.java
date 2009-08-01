package com.cedarsoft.utils;

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
