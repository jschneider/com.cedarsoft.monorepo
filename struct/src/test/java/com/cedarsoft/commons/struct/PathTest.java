package com.cedarsoft.commons.struct;

import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: 12.10.2006<br>
 * Time: 16:25:26<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class PathTest {
  @Test
  public void testSubPath() {
    Path path = new Path( "a", "b", "c", "d" );
    Path subPath = path.subPath( 0, 2 );
    assertEquals( new Path( "a", "b" ), subPath );
  }

  @Test
  public void testSerializeation() throws IOException, ClassNotFoundException {
    Path path = new Path( "a", "b" );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    new ObjectOutputStream( out ).writeObject( path );

    Object read = new ObjectInputStream( new ByteArrayInputStream( out.toByteArray() ) ).readObject();
    assertEquals( path, read );
  }

  @Test
  public void testEmptyPath() {
    assertEquals( Path.EMPTY.toString(), "" );
    assertEquals( Path.EMPTY.absolute().toString(), "/" );
    assertFalse( Path.EMPTY.isAbsolute() );

    assertEquals( Path.createPath( "" ).toString(), "" );
    assertEquals( Path.createPath( "" ), Path.EMPTY );
    assertEquals( Path.createPath( "/" ).toString(), "/" );
    assertTrue( Path.createPath( "/" ).isAbsolute(), "/" );
    assertEquals( Path.createPath( "/" ).size(), 0 );
  }

  @Test
  public void testPop() {
    Path path = new Path( "a", "b" );
    assertEquals( path.toString(), "a/b" );
    assertEquals( path.popped().toString(), "b" );
    assertEquals( path.toString(), "a/b" );
  }

  @Test
  public void testPopAbs() {
    Path path = new Path( Arrays.asList( "a", "b", "c" ), true );

    assertEquals( path.toString(), "/a/b/c" );
    assertEquals( path.popped().toString(), "b/c" );
    assertEquals( path.toString(), "/a/b/c" );
  }

  @Test
  public void testAbsolute() {
    Path path = new Path( "a", "b" );
    assertEquals( "a/b", path.toString() );

    assertEquals( "/a/b", path.absolute().toString() );
  }

  @Test
  public void testCreateParent() {
    assertEquals( "ab/asdf/a/b", Path.createPath( "asdf/a/b" ).withParent( "ab" ).toString() );

    //cannot create path for root
    try {
      Path child = Path.createPath( "/asdf/a/b" );
      assertTrue( child.isAbsolute() );
      child.withParent( "ab" );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }

    assertEquals( "/ab/asdf/a/b", Path.createPath( "asdf/a/b" ).withParent( "/ab" ).toString() );
  }

  @Test
  public void testCreateChildPath() {
    Path path = Path.createPath( "/asdf/a/b" );
    Path child = path.withChild( "c" );
    assertEquals( "asdf/a/b/c", child.toString() );
  }

  @Test
  public void testElements() {
    List<Node> nodes = new ArrayList<Node>();
    nodes.add( new DefaultNode( "a" ) );
    nodes.add( new DefaultNode( "b" ) );
    nodes.add( new DefaultNode( "c" ) );

    assertEquals( "a/b/c", Route.buildRoute( nodes ).toString() );
  }

  @Test
  public void testPathElements() {
    Path path = Path.createPath( "/asdf/a/b" );
    List<? extends String> elements = path.getElements();
    assertEquals( 3, elements.size() );
    assertEquals( "asdf", elements.get( 0 ) );
    assertEquals( "a", elements.get( 1 ) );
    assertEquals( "b", elements.get( 2 ) );
  }

  @Test
  public void testEquals() {
    assertEquals( Path.createPath( "/" ), Path.createPath( "/" ) );
    assertEquals( Path.createPath( "/asdf" ), Path.createPath( "/asdf" ) );
    assertEquals( Path.createPath( "/a/b" ), Path.createPath( "/a/b" ) );
    assertEquals( Path.createPath( "/a/" ), Path.createPath( "/a" ) );
  }

  @Test
  public void testStringSplit() {
    assertEquals( 0, "/".split( "/" ).length );
    assertEquals( 0, "/".split( "/" ).length );
  }

  @Test
  public void testPath() {
    Path path = Path.createPath( "/" );
    assertTrue( path.getElements().isEmpty() );
  }

  @Test
  public void testBuildPaths() {
    Node parent = new DefaultNode( "a" );
    Node child0 = new DefaultNode( "child0" );
    parent.addChild( child0 );

    Node child1 = new DefaultNode( "child1" );
    child0.addChild( child1 );

    Path path = Path.buildPath( child1 );
    assertEquals( "a/child0/child1", path.toString() );
  }

  @Test
  public void testPathToString() {
    Path path = new Path( "a", "b", "c" );
    assertEquals( "a/b/c", path.toString() );
  }

  @Test
  public void testEquals2() {
    assertEquals( new Path( "a", "b", "c" ), new Path( "a", "b", "c" ) );
    assertEquals( new Path( "a", "b", "c" ), new Path( "a", "b", "c" ) );
    assertFalse( new Path( "a", "b", "c" ).equals( new Path( "a", "b" ) ) );
  }

  @Test
  public void testCreatePath() {
    Path path = Path.createPath( "/a/b/c" );
    assertTrue( path.isAbsolute() );
    assertNotNull( path );
    assertEquals( 3, path.getElements().size() );
    assertEquals( "/a/b/c", path.toString() );
  }

  @Test
  public void testCreatePath2() {
    assertEquals( Path.createPath( "/a/b/c" ).toString(), "/a/b/c" );
    assertEquals( Path.createPath( "a/b/c" ).toString(), "a/b/c" );
    assertEquals( Path.createPath( "a" ).toString(), "a" );

    assertEquals( Path.createPath( "/a/" ).toString(), "/a" );
    assertEquals( Path.createPath( "/a/b/c/" ).toString(), "/a/b/c" );

    assertEquals( Path.createPath( "" ).toString(), "" );

    try {
      Path.createPath( "/a///" );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testToStringAbsolute() {
    assertEquals( Path.createPath( "/a/b" ).toString(), "/a/b" );
    assertTrue( Path.createPath( "/a/b" ).isAbsolute() );
    assertEquals( Path.createPath( "a/b" ).toString(), "a/b" );
    assertFalse( Path.createPath( "a/b" ).isAbsolute() );
  }
}
