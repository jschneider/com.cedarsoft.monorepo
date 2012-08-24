package com.cedarsoft.annotations.instrumentation;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UiThreadAnnotationTransformer extends AbstractUiThreadAnnotationTransformer {
  @Nonnull
  public static final String VERIFY_UI_THREAD = "com.cedarsoft.annotations.verification.VerifyUiThread.verifyUiThreadAsserted();";
//  public static final String VERIFY_UI_THREAD = "assert (com.cedarsoft.annotations.verification.VerifyUiThread.verifyUiThreadAsserted());";

  @Nonnull
  @Override
  protected String appendVerificationCode() {
    return VERIFY_UI_THREAD;
//    return "assert( true);";
  }
}
