package com.cedarsoft.registry;

import org.junit.*;

import java.util.Comparator;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 *
 */
public class TreeSetTest {
  @Test
  public void testIt() throws Exception {
    TreeSet<MyObject> set = new TreeSet<MyObject>( new Comparator<MyObject>() {
      @Override
      public int compare( MyObject o1, MyObject o2 ) {
        return Integer.valueOf( o1.id ).compareTo( o2.id );
      }
    } );

    set.add( new MyObject( 5 ) );
    set.add( new MyObject( 4 ) );
    set.add( new MyObject( 3 ) );

    assertEquals( 3, set.size() );

    assertTrue( set.contains( new MyObject( 3 ) ) );
  }

  public static class MyObject {
    private final int id;

    public MyObject( int id ) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    @Override
    public boolean equals( Object obj ) {
      throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
      throw new UnsupportedOperationException();
    }
  }
}
