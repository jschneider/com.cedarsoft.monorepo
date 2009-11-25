package com.cedarsoft.inject;

import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

/**
 *
 */
public class GuiceModulesHelperTest {
  @Test
  public void testFail() {
    try {
      GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), List.class );
      fail( "Where is the Exception" );
    } catch ( ConfigurationException ignore ) {
    }
  }

  @Test
  public void testMinimization() {
    GuiceModulesHelper.Result result = GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), Object.class );
    assertEquals( result.getRemoved().size(), 2 );
    assertEquals( result.getTypes().size(), 0 );
  }

  @Test
  public void testMinimization2() {
    GuiceModulesHelper.Result result = GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), MyObject.class );
    assertEquals( result.getRemoved().size(), 1 );
    assertEquals( result.getTypes().size(), 1 );
  }

  public static class Module1 extends AbstractModule {
    @Override
    protected void configure() {
      bind( MyObject.class ).toInstance( new MyObject( "theId" ) );
    }
  }

  public static class Module2 extends AbstractModule {
    @Override
    protected void configure() {
    }
  }

  public static class MyObject {
    private final String id;

    public MyObject( String id ) {
      this.id = id;
    }
  }
}
