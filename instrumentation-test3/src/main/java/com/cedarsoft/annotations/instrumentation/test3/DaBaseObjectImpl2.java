package com.cedarsoft.annotations.instrumentation.test3;

import com.cedarsoft.annotations.NonUiThread;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DaBaseObjectImpl2 extends DaBaseObject {
  @NonUiThread
  public void doit3() {
    System.out.println( "---" );
  }

  @NonUiThread
  @Override
  public void doIt() {
    super.doIt();
    System.out.println( "---" );
  }

  @NonUiThread
  public void doit4() {
    System.out.println( "---" );
  }

}
