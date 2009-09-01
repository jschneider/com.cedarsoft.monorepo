package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Collections;

/**
 *
 */
public class RouteTest {
  private DefaultNode root;
  private DefaultNode nodeA;
  private DefaultNode nodeB;
  private DefaultNode nodeC;

  @BeforeMethod
  protected void setUp() throws Exception {
    root = new DefaultNode( "asdf" );
    nodeA = new DefaultNode( "a" );
    root.addChild( nodeA );
    nodeB = new DefaultNode( "b" );
    nodeA.addChild( nodeB );
    nodeC = new DefaultNode( "c" );
  }

  @Test
  public void testEmptyRoute() {
    Route route = new Route( Collections.<Node>emptyList() );
    assertEquals( route.size(), 0 );
  }

  @Test
  public void testEmptyRoute2() {
    assertEquals( Route.buildRoute( root, Path.EMPTY ).size(), 1 );

    try {
      Route.buildRoute( root, Path.EMPTY.absolute() );
      fail("Where is the Exception");
    } catch ( IllegalArgumentException ignore ) {
    }
  }

  @Test
  public void testInvalidRoot() {
    try {
      Route.buildRoute( new DefaultNode( "invalid" ), Path.createPath( "/asdf/a/b" ) );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }
  }

  @Test
  public void testCreate() {
    assertEquals( root.getChildren().size(), 1 );

    Path path = Path.createPath( "z" );
    try {
      Route.buildRoute( root, path );
      fail( "Where is the Exception" );
    } catch ( ChildNotFoundException ignore ) {
    }

    Route route = Route.buildRoute( root, path, new NodeFactory() {
      @NotNull
      public Node createNode( @NotNull @NonNls String name ) {
        return new DefaultNode( name );
      }
    } );

    assertEquals( route.size(), 2 );
    assertEquals( route.getNodes().get( 0 ), root );
    assertEquals( route.getNodes().get( 1 ).getName(), "z" );
    assertEquals( root.getChildren().size(), 2 );
  }

  @Test
  public void testRouteWithRelative() throws Exception {
    Route route = Route.buildRoute( root, Path.createPath( "a/b" ) );
    assertEquals( route.size(), 3 );

    assertEquals( route.getLastNode().getName(), "b" );
    assertEquals( route.getNodes().get( 0 ).getName(), "asdf" );
    assertEquals( route.getNodes().get( 1 ).getName(), "a" );
    assertEquals( route.getNodes().get( 2 ).getName(), "b" );
  }

  @Test
  public void testCreateChildPath() throws ChildNotFoundException {
    Route route = Route.buildRoute( root, Path.createPath( "/asdf/a/b" ) );
    assertNotNull( route );

    assertEquals( 3, route.getNodes().size() );
    assertSame( root, route.getNodes().get( 0 ) );
    assertSame( nodeA, route.getNodes().get( 1 ) );
    assertSame( nodeB, route.getNodes().get( 2 ) );

    assertEquals( "asdf/a/b", route.getPath().toString() );
  }

  @Test
  public void testEquals() throws ChildNotFoundException {
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf" ) ), Route.buildRoute( root, Path.createPath( "/asdf" ) ) );
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a" ) ), Route.buildRoute( root, Path.createPath( "a" ) ) );
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a/b" ) ), Route.buildRoute( root, Path.createPath( "a/b" ) ) );

    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a/b" ) ), Route.buildRoute( root, Path.createPath( "a/b" ) ) );
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a/b/" ) ), Route.buildRoute( root, Path.createPath( "a/b" ) ) );
  }
}
