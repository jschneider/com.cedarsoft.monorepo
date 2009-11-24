package com.cedarsoft.cache;

import org.jetbrains.annotations.NotNull;
import org.testng.*;
import org.testng.annotations.*;

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
    Assert.assertEquals( "3", cache.get( 3 ) );
    Assert.assertEquals( 1, cache.entrySet().size() );
    Iterator<Map.Entry<Integer, String>> iterator = cache.entrySet().iterator();
    Assert.assertTrue( iterator.hasNext() );
    iterator.next();
    iterator.remove();
    Assert.assertEquals( 0, cache.entrySet().size() );
  }

  @Test
  public void testIt() {
    Assert.assertEquals( 0, counter );
    Assert.assertEquals( 0, cache.size() );
    Assert.assertEquals( "5", cache.get( 5 ) );
    Assert.assertEquals( "4", cache.get( 4 ) );
    Assert.assertEquals( "4", cache.get( 4 ) );

    Assert.assertEquals( 2, cache.size() );
    Assert.assertEquals( 2, counter );
  }
}
