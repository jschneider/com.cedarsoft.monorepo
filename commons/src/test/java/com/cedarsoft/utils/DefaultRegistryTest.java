package com.cedarsoft.utils;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.lang.Override;
import java.util.Comparator;

/**
 *
 */
public class DefaultRegistryTest {
  @Test
  public void testIt() {
    DefaultRegistry<String> registry = new DefaultRegistry<String>();
    assertEquals( registry.getStoredObjects().size(), 0 );

    registry.store( "1" );
    registry.store( "1" );
    assertEquals( registry.getStoredObjects().size(), 2 );
  }

  @Test
  public void testUnique() {
    DefaultRegistry<String> registry = new DefaultRegistry<String>( new Comparator<String>() {
      @Override
      public int compare( String o1, String o2 ) {
        return o1.compareTo( o2 );
      }
    } );
    assertEquals( registry.getStoredObjects().size(), 0 );

    registry.store( "1" );
    try {
      registry.store( "1" );
      fail( "Where is the Exception" );
    } catch ( StillContainedException ignore ) {
    }
    assertEquals( registry.getStoredObjects().size(), 1 );
  }
}
