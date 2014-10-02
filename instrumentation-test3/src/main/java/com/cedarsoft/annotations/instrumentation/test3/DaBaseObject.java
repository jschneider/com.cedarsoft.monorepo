package com.cedarsoft.annotations.instrumentation.test3;

import com.cedarsoft.annotations.UiThread;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class DaBaseObject {
  @UiThread
  public void doIt() {
    assert true;
    System.out.println( "---" );
  }

  @UiThread
  public void doIt2() {
    assert true;
    System.out.println( "---" );
  }
}
