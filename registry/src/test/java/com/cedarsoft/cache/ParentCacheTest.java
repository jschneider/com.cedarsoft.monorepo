package com.cedarsoft.cache;

import com.cedarsoft.cache.ParentCache;
import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 * Stores the parent of children in a weak map
 */
public class ParentCacheTest {
  private ParentCache cache;

  @BeforeMethod
  protected void setUp() throws Exception {
    cache = new ParentCache();
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testIndex() {
    cache.storeIndex( "c", 1 );
    assertEquals( new Integer( 1 ), cache.findIndex( "c" ) );

    cache.storeIndex( "c", 1 );
    cache.storeIndex( "c", 2 );
    assertEquals( new Integer( 2 ), cache.findIndex( "c" ) );

    cache.remove( "c" );
    assertNull( cache.findIndex( "c" ) );
    cache.storeIndex( "c", 3 );
    assertEquals( new Integer( 3 ), cache.findIndex( "c" ) );
  }

  @Test
  public void testRemove() {
    String child = "child";
    cache.store( child, "parent", 0 );
    assertSame( "parent", cache.findParent( child ) );

    cache.remove( child );
    assertNull( cache.findParent( child ) );
  }

  @Test
  public void testOverwrite() {
    String child = "child";
    cache.store( child, "parent", 0 );
    cache.store( child, "parent", 0 );
    try {
      cache.store( child, "parent2", 0 );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testSimple() {
    String child = "asdf";
    assertNull( cache.findParent( child ) );
    String parent = "parent";
    cache.store( child, parent, 0 );
    assertSame( parent, cache.findParent( child ) );
  }

  @Test
  public void testWeak() {
    Object child = new Object();
    assertNull( cache.findParent( child ) );
    fill( child );
    assertNotNull( cache.findParent( child ) );
    System.gc();
    assertNull( cache.findParent( child ) );
  }

  private void fill( @NotNull Object child ) {
    cache.store( child, new Object(), 0 );
  }
}
