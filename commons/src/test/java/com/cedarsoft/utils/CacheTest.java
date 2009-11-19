package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class CacheTest {
  private HashedCache<Integer, String> cache;
  private int counter;

  @BeforeMethod
  protected void setUp() throws Exception {
    cache = new HashedCache<Integer, String>( new Cache.Factory<Integer, String>() {
      @Override
      @NotNull
      public String create( @NotNull Integer key ) {
        counter++;
        return String.valueOf( key );
      }
    } );
    counter = 0;
  }

  @Test
  public void testRemove() {
    assertEquals( "3", cache.get( 3 ) );
    assertEquals( 1, cache.entrySet().size() );
    Iterator<Map.Entry<Integer, String>> iterator = cache.entrySet().iterator();
    assertTrue( iterator.hasNext() );
    iterator.next();
    iterator.remove();
    assertEquals( 0, cache.entrySet().size() );
  }

  @Test
  public void testIt() {
    assertEquals( 0, counter );
    assertEquals( 0, cache.size() );
    assertEquals( "5", cache.get( 5 ) );
    assertEquals( "4", cache.get( 4 ) );
    assertEquals( "4", cache.get( 4 ) );

    assertEquals( 2, cache.size() );
    assertEquals( 2, counter );
  }
}
