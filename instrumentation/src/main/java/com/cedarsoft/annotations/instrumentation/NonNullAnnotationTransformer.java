package com.cedarsoft.annotations.instrumentation;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NonNullAnnotationTransformer extends AbstractAnnotationTransformer {
  @Override
  protected void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException, NotFoundException, BadBytecode {
    for ( CtMethod method : ctClass.getMethods() ) {
      if ( method.isEmpty() ) {
        continue;
      }

      //Checking for return values
      boolean nonNullAnnotation = isAnnotated( method, Nonnull.class );
      boolean nullAnnotation = isAnnotated( method, Nullable.class );

      if ( nonNullAnnotation && nullAnnotation ) {
        throw new IllegalStateException( "Must not have both annotations in " + ctClass.getName() + "#" + method.getName() + ": " + Nonnull.class.getName() + " and " + Nullable.class.getName() );
      }

      if ( nonNullAnnotation ) {
        insertAssertedVerificationCodeAfter( method, "{com.cedarsoft.annotations.verification.VerifyNonNull.verifyNonNull($_, \"Return value must not be null for method annotated with @Nonnull\");}" );
      }
      //if ( nonUiThread ) {
      //  insertAssertedVerificationCodeBefore( method, getNonUiThreadVerificationCode() );
      //}
    }
  }
}
