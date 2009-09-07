package com.cedarsoft.utils;

import com.cedarsoft.EasyMockTemplate;
import org.easymock.classextension.EasyMock;
import static org.testng.Assert.*;
import org.testng.annotations.*;

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
  public void testListener() throws Exception {
    final DefaultRegistry.Listener<String> listener = EasyMock.createMock( DefaultRegistry.Listener.class );


    new EasyMockTemplate( listener ) {
      @Override
      protected void expectations() throws Exception {
        listener.objectAdded( "asdf" );
      }

      @Override
      protected void codeToTest() throws Exception {
        registry.addListener( listener );
        registry.store( "asdf" );
      }
    }.run();
  }
}
