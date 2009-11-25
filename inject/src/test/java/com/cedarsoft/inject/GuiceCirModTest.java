package com.cedarsoft.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Provides;
import org.testng.annotations.*;

import java.util.Arrays;

import static org.testng.Assert.*;

/**
 *
 */
public class GuiceCirModTest {
  @Test
  public void testIt() {
    MyObject myObject = Guice.createInjector( new Module1(), new Module2() ).getInstance( MyObject.class );
    assertEquals( myObject.id, 7 );

    GuiceModulesHelper.Result result = GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), MyObject.class );
    assertEquals( result.getRemoved().size(), 0 );
  }

  public static class Module1 extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    public Integer providesInteger( String string ) {
      assertEquals( string, "magic" );
      return 7;
    }

  }

  public static class Module2 extends AbstractModule {
    @Override
    protected void configure() {
      bind( String.class ).toInstance( "magic" );
    }
  }

  public static class MyObject {
    private final int id;

    @Inject
    public MyObject( Integer id ) {
      this.id = id;
    }
  }

}
