package com.cedarsoft.utils;

import com.cedarsoft.EasyMockTemplate;
import org.easymock.classextension.EasyMock;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 */
public class RegistryTest {
  private Registry<String> registry;

  @BeforeMethod
  protected void setUp() throws Exception {
    registry = new DefaultRegistry<String>();
  }

  @Test
  public void testIt() {
    assertTrue( registry.getStoredObjects().isEmpty() );
    registry.store( "asdf" );
    assertFalse( registry.getStoredObjects().isEmpty() );
    assertEquals( registry.getStoredObjects().size(), 1 );
  }

  @Test
  public void addExisting() {
    assertTrue( registry.getStoredObjects().isEmpty() );
    registry.store( "asdf" );
    assertEquals( registry.getStoredObjects().size(), 1 );
    registry.store( "asdf" );
    assertEquals( registry.getStoredObjects().size(), 2 );

    registry = new DefaultRegistry<String>( new Comparator<String>() {
      public int compare( String o1, String o2 ) {
        return o1.compareTo( o2 );
      }
    } );

    registry.store( "asdf" );
    assertEquals( registry.getStoredObjects().size(), 1 );
    try {
      registry.store( "asdf" );
      fail("Where is the Exception");
    } catch ( StillContainedException ignore ) {
    }
    assertEquals( registry.getStoredObjects().size(), 1 );
  }

  @Test
  public void testInitiWithComp() {
    try {
      new DefaultRegistry<String>( Arrays.asList( "a", "b", "a" ), new Comparator<String>() {
        public int compare( String o1, String o2 ) {
          return o1.compareTo( o2 );
        }
      } );
      fail("Where is the Exception");
    } catch ( StillContainedException ignore ) {
    }
  }

  @Test
  public void testListener() throws Exception {
    final DefaultRegistry.Listener<String> listener = EasyMock.createMock( DefaultRegistry.Listener.class );


    new EasyMockTemplate( listener ) {
      @Override
      protected void expectations() throws Exception {
        listener.objectStored( "asdf" );
      }

      @Override
      protected void codeToTest() throws Exception {
        registry.addListener( listener );
        registry.store( "asdf" );
      }
    }.run();
  }
}
