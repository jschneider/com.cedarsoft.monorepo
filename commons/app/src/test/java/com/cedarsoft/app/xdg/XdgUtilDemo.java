package com.cedarsoft.app.xdg;

import com.google.common.base.StandardSystemProperty;
import org.junit.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class XdgUtilDemo {
  @Test
  public void testIt() throws Exception {
    String value = StandardSystemProperty.OS_NAME.value();
    if ( !value.contains( "Linux" ) ) {
      System.err.println( "Not running on Linux!" );
      return;
    }

    System.err.println( "Config Home: " + XdgUtil.getConfigHome().getAbsolutePath() );
    System.err.println( "Cache Home: " + XdgUtil.getCacheHome().getAbsolutePath() );
    System.err.println( "Data Home: " + XdgUtil.getDataHome().getAbsolutePath() );
  }
}