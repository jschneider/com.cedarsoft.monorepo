package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents route of several nodes.
 */
public class Route {
  @NotNull
  private static final Route EMPTY = new Route( Collections.<Node>emptyList() );
  @NotNull
  private final List<Node> nodes = new ArrayList<Node>();

  /**
   * Creates a new route
   *
   * @param nodes the nodes
   */
  public Route( @NotNull List<? extends Node> nodes ) {
    this.nodes.addAll( nodes );
  }

  /**
   * Builds a path for several nodes
   *
   * @param nodes the nodes
   * @return the path
   */
  @NotNull
  public static Path buildRoute( @NotNull List<? extends Node> nodes ) {
    List<String> elements = new ArrayList<String>();
    for ( Node node : nodes ) {
      elements.add( node.getName() );
    }
    return new Path( elements );
  }

  /**
   * Returns the last node of the route
   *
   * @return the last node
   *
   * @throws IllegalStateException if the route is empty
   */
  @NotNull
  public Node getLastNode() throws IllegalStateException {
    if ( nodes.isEmpty() ) {
      throw new IllegalStateException( "Path has no nodes" );
    }
    return nodes.get( nodes.size() - 1 );
  }

  /**
   * Returns the nodes
   *
   * @return the nodes for this route
   */
  @NotNull
  public List<? extends Node> getNodes() {
    return Collections.unmodifiableList( nodes );
  }

  public int size() {
    return nodes.size();
  }

  /**
   * Returns the path
   *
   * @return the path
   */
  @NotNull
  public Path getPath() {
    List<String> elements = new ArrayList<String>();
    for ( Node node : nodes ) {
      elements.add( node.getName() );
    }
    return new Path( elements );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    Route route = ( Route ) o;

    if ( !nodes.equals( route.nodes ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return nodes.hashCode();
  }

  /**
   * Build a route
   *
   * @param rootNode the root node
   * @param path     the path
   * @return the route
   *
   * @throws ChildNotFoundException
   */
  @NotNull
  public static Route buildRoute( @NotNull Node rootNode, @NotNull Path path ) throws ChildNotFoundException {
    return buildRouteInternal( rootNode, path, null );
  }

  /**
   * Builds the route. Creates and add all necessary nodes
   *
   * @param rootNode    the root
   * @param path        the path
   * @param nodeFactory the node factory that creates the nodes
   * @return the route
   */
  @NotNull
  public static Route buildRoute( @NotNull Node rootNode, @NotNull Path path, @NotNull NodeFactory nodeFactory ) {
    return buildRouteInternal( rootNode, path, nodeFactory );
  }

  @NotNull
  public static Route buildRouteInternal( @NotNull Node rootNode, @NotNull Path path, @Nullable NodeFactory nodeFactory ) throws ChildNotFoundException {
    if ( path.size() == 0 ) {
      return Route.EMPTY;
    }

    Iterator<? extends String> iterator = path.getElements().iterator();
    //If the path is absolute, we have to verify the root node
    if ( path.isAbsolute() ) {
      String firstElement = iterator.next();
      if ( !rootNode.getName().equals( firstElement ) ) {
        throw new IllegalArgumentException( "Invalid root node. Expected \"" + path.getElements().get( 0 ) + "\" but was \"" + rootNode.getName() + "\"" );
      }
    }

    List<Node> nodes = new ArrayList<Node>();
    nodes.add( rootNode );
    while ( iterator.hasNext() ) {
      String element = iterator.next();
      Node lastNode = nodes.get( nodes.size() - 1 );
      try {
        nodes.add( lastNode.findChild( element ) );
      } catch ( ChildNotFoundException e ) {
        //Throw the exception if no node factory is available
        if ( nodeFactory == null ) {
          throw e;
        } else {
          Node created = nodeFactory.createNode( element );
          lastNode.addChild( created );
          nodes.add( created );
        }
      }
    }

    return new Route( nodes );
  }
}
