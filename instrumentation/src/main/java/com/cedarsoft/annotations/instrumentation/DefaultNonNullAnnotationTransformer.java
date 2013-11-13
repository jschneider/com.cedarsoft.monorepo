package com.cedarsoft.annotations.instrumentation;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.WillClose;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DefaultNonNullAnnotationTransformer extends AbstractAnnotationTransformer {
  @Nonnull
  protected final String nonNullReturnValueCode;
  @Nonnull
  protected final String nonNullParamCode;

  public DefaultNonNullAnnotationTransformer( @WillClose @Nonnull InputStream nonNullReturnValueCodeInput, @WillClose @Nonnull InputStream nonNullParamCodeInput ) throws IOException {
    try {
      nonNullReturnValueCode = new String( ByteStreams.toByteArray( nonNullReturnValueCodeInput ), Charsets.UTF_8 );
    } finally {
      nonNullReturnValueCodeInput.close();
    }

    try {
      nonNullParamCode = new String( ByteStreams.toByteArray( nonNullParamCodeInput ), Charsets.UTF_8 );
    } finally {
      nonNullReturnValueCodeInput.close();
    }
  }

  public DefaultNonNullAnnotationTransformer( @Nonnull String nonNullReturnValueCode, @Nonnull String nonNullParamCode ) {
    this.nonNullReturnValueCode = nonNullReturnValueCode;
    this.nonNullParamCode = nonNullParamCode;
  }

  @Override
  protected void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException, NotFoundException {
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

      if ( nonNullAnnotation && !method.getReturnType().isPrimitive() ) {
        insertAssertedVerificationCodeAfter( method, getNonNullReturnValueCode() );
      }

      //Now let's check for the parameters
      transformParameters( method );
    }


    //Transform the parameters for the constructors
    for ( CtConstructor constructor : ctClass.getConstructors() ) {
      transformParameters( constructor );
    }
  }

  protected void transformParameters( @Nonnull CtBehavior method ) throws ClassNotFoundException, NotFoundException, CannotCompileException {
    Object[][] parameterAnnotations = method.getParameterAnnotations();
    for ( int i = 0, parameterAnnotationsLength = parameterAnnotations.length; i < parameterAnnotationsLength; i++ ) {
      if ( AnnotationUtils.hasAnnotation( parameterAnnotations[i], Nonnull.class ) ) {
        //Skip primitive parameters
        if ( method.getParameterTypes()[i].isPrimitive() ) {
          continue;
        }

        int parameterNumber = i + 1;
        String format = MessageFormat.format( getNonNullParamCode(), parameterNumber );
        insertAssertedVerificationCodeBefore( method, format );
      }
    }
  }

  @Nonnull
  protected String getNonNullParamCode() {
    return nonNullParamCode;
  }

  @Nonnull
  protected String getNonNullReturnValueCode() {
    return nonNullReturnValueCode;
  }
}
