package com.cedarsoft.annotations.instrumentation.test3;

import com.cedarsoft.annotations.NonUiThread;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DaBaseObjectImpl1 extends DaBaseObject {
  @NonUiThread
  public void doit3() {
    assert true;
    System.out.println( "---" );
  }

  @NonUiThread
  public void doit4() {
    assert true;
    System.out.println( "---" );
  }

}
