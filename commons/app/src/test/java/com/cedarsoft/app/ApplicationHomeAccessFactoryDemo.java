package com.cedarsoft.app;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ApplicationHomeAccessFactoryDemo {
  @Test
  public void testIt() throws Exception {
    ApplicationHomeAccess homeAccess = ApplicationHomeAccessFactory.create( "my-test-app" );

    System.out.println( "Config Home: " + homeAccess.getConfigHome() );
    System.out.println( "Data Home: " + homeAccess.getDataHome() );
    System.out.println( "Cache Home: " + homeAccess.getCacheHome() );
  }
}