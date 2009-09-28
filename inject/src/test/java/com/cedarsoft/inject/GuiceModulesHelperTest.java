package com.cedarsoft.inject;

import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class GuiceModulesHelperTest {
  @Test
  public void testFail() {
    try {
      GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), List.class );
      fail("Where is the Exception");
    } catch ( ConfigurationException ignore ) {
    }
  }

  @Test
  public void testMinimization() {
    GuiceModulesHelper.Result result = GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), Object.class );
    assertEquals( result.getRemoved().size(), 2 );
    assertEquals( result.getTypes().size(), 0 );
  }

  public static class Module1 extends AbstractModule {
    @Override
    protected void configure() {
    }
  }

  public static class Module2 extends AbstractModule {
    @Override
    protected void configure() {
    }
  }
}
