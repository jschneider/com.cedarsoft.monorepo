package com.cedarsoft.annotations.instrumentation;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UiThreadAnnotationTransformer extends AbstractAnnotationTransformer {
  @Nonnull
  private static String getUiThreadVerificationCode() {
    return "com.cedarsoft.annotations.verification.VerifyUiThread.verifyUiThread();";
  }

  @Nonnull
  protected static String getNonUiThreadVerificationCode() {
    return "com.cedarsoft.annotations.verification.VerifyUiThread.verifyNonUiThread();";
  }

  @Override
  protected void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException {
    for ( CtMethod method : ctClass.getMethods() ) {
      if ( method.isEmpty() ) {
        continue;
      }

      boolean uiThread = isAnnotated( method, UiThread.class );
      boolean nonUiThread = isAnnotated( method, NonUiThread.class );

      if ( uiThread && nonUiThread ) {
        throw new IllegalStateException( "Must not have both annotations in " + ctClass.getName() + "#" + method.getName() + ": " + UiThread.class.getName() + " and " + NonUiThread.class.getName() );
      }

      if ( uiThread ) {
        insertAssertedVerificationCode( method, getUiThreadVerificationCode() );
      }
      if ( nonUiThread ) {
        insertAssertedVerificationCode( method, getNonUiThreadVerificationCode() );
      }
    }
  }
}
