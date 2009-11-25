package com.cedarsoft.lookup;

import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class InstantiatorLookupTest {
  private InstantiatorLookup<String> lookup;
  private boolean called;

  @BeforeMethod
  protected void setUp() throws Exception {
    Instantiater<String> instantiater = new Instantiater<String>() {
      @Override
      @NotNull
      public String createInstance() throws InstantiationFailedException {
        if ( called ) {
          throw new IllegalStateException();
        }
        called = true;
        return "asdf";
      }
    };
    lookup = new InstantiatorLookup<String>( String.class, instantiater );
  }

  @Test
  public void testIt() {
    assertFalse( called );
    assertEquals( 4, lookup.lookups().size() );
    assertFalse( called );
    assertNull( lookup.lookup( Integer.class ) );
    assertFalse( called );
    assertEquals( "asdf", lookup.lookup( String.class ) );
    assertTrue( called );
    assertEquals( "asdf", lookup.lookup( String.class ) );
    assertTrue( called );
  }
}
