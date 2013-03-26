package com.cedarsoft.annotations.instrumentation;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

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
        insertAssertedVerificationCodeAfter( method, "{com.cedarsoft.annotations.verification.VerifyNonNull.verifyNonNullReturnValue($_);}" );
      }


      //Now let's check for the parameters
      Object[][] parameterAnnotations = method.getParameterAnnotations();
      for ( int i = 0, parameterAnnotationsLength = parameterAnnotations.length; i < parameterAnnotationsLength; i++ ) {
        if ( hasAnnotation( parameterAnnotations[i], Nonnull.class ) ) {
          int parameterNumber = i + 1;
          method.insertBefore( "System.out.println(\"$" + parameterNumber + "\"+$" + parameterNumber + ");" );

          insertAssertedVerificationCodeBefore( method, "{com.cedarsoft.annotations.verification.VerifyNonNull.verifyNonNullParameter($"+parameterNumber+", "+parameterNumber+");}" );
        }
      }
    }
  }

  private static boolean hasAnnotation( @Nonnull Object[] annotations, @Nonnull Class<?> annotationType ) {
    for ( Object annotation : annotations ) {
      if ( annotationType.isAssignableFrom( annotation.getClass() ) ) {
        return true;
      }
    }
    return false;
  }
}
