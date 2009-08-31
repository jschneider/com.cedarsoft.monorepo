package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a path of several nodes.
 * A path is a string representation.
 * <p/>
 * If you want to get a "list" of nodes try {@link Route} instead.
 * <p/>
 * A path is *immutable*
 */
public class Path implements Serializable {
  private static final long serialVersionUID = -201844652320294645L;

  @NonNls
  public static final char PATH_SEPARATOR_CHAR = '/';
  @NotNull
  @NonNls
  public static final String PATH_SEPARATOR = String.valueOf( PATH_SEPARATOR_CHAR );

  /**
   * Creates a path for a node
   *
   * @param node the node
   * @return the path for the given node
   */
  @NotNull
  public static Path buildPath( @NotNull Node node ) {
    return PathFactory.buildPath( node );
  }

  /**
   * Create a path object for a given path representation
   *
   * @param pathRepresentation the path representation
   * @return the path
   */
  @NotNull
  public static Path createPath( @NotNull @NonNls String pathRepresentation ) {
    return PathFactory.createPath( pathRepresentation );
  }

  private final boolean absolute;
  @NotNull
  @NonNls
  private final List<String> elements = new ArrayList<String>();

  /**
   * Creates a new path
   *
   * @param elements the elements
   */
  public Path( @NotNull String... elements ) {
    this( Arrays.asList( elements ) );
  }

  @TestOnly
  @Deprecated
  public Path( boolean relative, @NotNull String... elements ) {
    this( Arrays.asList( elements ), relative );
  }

  /**
   * Creates a new *relative* path
   *
   * @param elements the elements
   */
  public Path( @NotNull List<? extends String> elements ) {
    this( elements, false );
  }

  public Path( @NotNull List<? extends String> elements, boolean absolute ) {
    this.absolute = absolute;
    if ( elements.isEmpty() ) {
      throw new IllegalArgumentException( "elements.size must not be 0" );
    }

    for ( String element : elements ) {
      if ( element.length() == 0 ) {
        continue;
      }
      this.elements.add( element );
    }
  }

  /**
   * Return a list containing all path elements
   *
   * @return a list containing all path elements
   */
  @NotNull
  @NonNls
  public List<? extends String> getElements() {
    return Collections.unmodifiableList( elements );
  }

  @NotNull
  @NonNls
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    if ( isAbsolute() ) {
      buffer.append( PATH_SEPARATOR_CHAR );
    }

    for ( String element : elements ) {
      buffer.append( element );
      buffer.append( PATH_SEPARATOR );
    }

    if ( buffer.length() > 1 ) {
      buffer.deleteCharAt( buffer.length() - 1 );
    }

    return buffer.toString();
  }

  public int size() {
    return elements.size();
  }

  public boolean isAbsolute() {
    return absolute;
  }

  @NotNull
  public Path absolute() {
    return absolute( true );
  }

  @NotNull
  public Path relative() {
    return absolute( false );
  }

  @NotNull
  public Path absolute( boolean newAbsolute ) {
    if ( absolute == newAbsolute ) {
      return this;
    }
    return new Path( elements, newAbsolute );
  }

  @NotNull
  public Path subPath( int index0, int index1 ) {
    return new Path( elements.subList( index0, index1 ) );
  }

  /**
   * Creates a new path where the first element has been removed
   *
   * @return a new path without the first element
   */
  @NotNull
  public Path popped() {
    if ( elements.isEmpty() ) {
      throw new ArrayIndexOutOfBoundsException( "No elements left to pop" );
    }
    return subPath( 1, elements.size() );
  }

  @NotNull
  public Path withParent( @NotNull @NonNls String parentPathRepresentation ) {
    return PathFactory.createParentPath( parentPathRepresentation, this );
  }

  /**
   * Creates a new path that represents the child with the given name
   *
   * @param childName the name of the child
   * @return the path of the child
   */
  public Path withChild( @NotNull String childName ) {
    List<String> childElements = new ArrayList<String>( elements.size() + 1 );
    childElements.addAll( elements );
    childElements.add( childName );
    return new Path( childElements );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    Path path = ( Path ) o;

    if ( elements != null ? !elements.equals( path.elements ) : path.elements != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return ( elements != null ? elements.hashCode() : 0 );
  }
}
