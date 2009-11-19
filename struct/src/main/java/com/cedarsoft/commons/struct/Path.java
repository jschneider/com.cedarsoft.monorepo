package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
  @NotNull
  public static final Path EMPTY = new Path();

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
  public Path( @NotNull Iterable<? extends String> elements ) {
    this( elements, false );
  }

  public Path( @NotNull Iterable<? extends String> elements, boolean absolute ) {
    this.absolute = absolute;

    for ( String element : elements ) {
      if ( element.length() == 0 ) {
        continue;
      }

      if ( element.contains( PATH_SEPARATOR ) ) {
        throw new IllegalArgumentException( "Invalid character (" + PATH_SEPARATOR + ") in <" + element + '>' );
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
    return createParentPath( parentPathRepresentation, this );
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
    return elements != null ? elements.hashCode() : 0;
  }

  /**
   * Create a path object for a given path representation
   *
   * @param pathRepresentation the path representation
   * @return the path
   */
  @NotNull
  public static Path createPath( @NonNls @NotNull String pathRepresentation ) {
    boolean isAbsolute = false;
    if ( pathRepresentation.startsWith( PATH_SEPARATOR ) ) {
      //noinspection AssignmentToMethodParameter
      pathRepresentation = pathRepresentation.substring( 1 );
      isAbsolute = true;
    }

    List<String> parts = new ArrayList<String>();

    StringBuilder actualName = new StringBuilder();
    for ( char actualChar : pathRepresentation.toCharArray() ) {
      if ( actualChar == PATH_SEPARATOR_CHAR ) {
        if ( actualName.length() == 0 ) {
          throw new IllegalArgumentException( "Name must not be empty: " + pathRepresentation );
        }

        parts.add( actualName.toString() );
        actualName = new StringBuilder();
      } else {
        actualName.append( actualChar );
      }
    }

    parts.add( actualName.toString() );

    return new Path( parts, isAbsolute );
  }

  /**
   * Creates a path for a node
   *
   * @param node the node
   * @return the path for the given node
   */
  @NotNull
  public static Path buildPath( @NotNull StructPart node ) {
    return buildPath( node, null );
  }

  /**
   * Creates a path for a node
   *
   * @param node      the node
   * @param validator the validator
   * @return the path for the given node
   */
  @NotNull
  public static Path buildPath( @NotNull StructPart node, @Nullable PathValidator validator ) {
    List<String> elements = new ArrayList<String>();

    //add parent node
    elements.add( 0, node.getName() );

    StructPart actualNode = node;
    while ( ( actualNode = actualNode.getParent() ) != null ) {
      elements.add( 0, actualNode.getName() );
    }
    Path path = new Path( elements );
    if ( validator != null ) {
      validator.validate( path );
    }
    return path;
  }

  /**
   * Creates a parent path
   *
   * @param parentPathRepresentation the string representation of the path of the parent
   * @param child                    the child path
   * @return the path for the parent string representation and the child
   */
  @NotNull
  public static Path createParentPath( @NotNull String parentPathRepresentation, @NotNull Path child ) {
    if ( child.isAbsolute() ) {
      throw new IllegalArgumentException( "Cannot create parent path for absolute child path" );
    }
    Path parentPath = createPath( parentPathRepresentation );

    List<String> elements = new ArrayList<String>( parentPath.getElements() );
    elements.addAll( child.getElements() );

    return new Path( elements, parentPath.isAbsolute() );
  }

  /**
   * Calculates the level of the given node relative to the root.
   * If the node is a child of root, the returned level is "1".
   *
   * @param root the root
   * @param node the node
   * @return the level
   */
  public static int calculateLevel( @NotNull StructPart root, @NotNull StructPart node ) {
    if ( root.equals( node ) ) {
      return 0;
    }

    int level = 1;

    StructPart currentParent = node.getParent();
    while ( currentParent != null ) {
      if ( currentParent.equals( root ) ) {
        return level;
      }
      currentParent = currentParent.getParent();
      level++;
    }

    throw new IllegalArgumentException( "Root is not parent of node: " + root + " - " + node );
  }
}
