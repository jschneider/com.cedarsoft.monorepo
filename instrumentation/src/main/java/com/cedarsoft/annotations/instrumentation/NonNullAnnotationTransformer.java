package com.cedarsoft.annotations.instrumentation;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NonNullAnnotationTransformer extends AbstractAnnotationTransformer {
  @Nonnull
  public static final String NON_NULL_RETURN_VALUE;
  @Nonnull
  public static final String NON_NULL_PARAM;

  static {
    try {
      {
        InputStream resourceAsStream = NonNullAnnotationTransformer.class.getResourceAsStream( "nonNullReturnValue.txt" );
        try {
          NON_NULL_RETURN_VALUE = new String( ByteStreams.toByteArray( resourceAsStream ), Charsets.UTF_8 );
        } finally {
          resourceAsStream.close();
        }
      }
      {
        InputStream resourceAsStream = NonNullAnnotationTransformer.class.getResourceAsStream( "nonNullParam.txt" );
        try {
          NON_NULL_PARAM = new String( ByteStreams.toByteArray( resourceAsStream ), Charsets.UTF_8 );
        } finally {
          resourceAsStream.close();
        }
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
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
        insertAssertedVerificationCodeAfter( method, NON_NULL_RETURN_VALUE );
      }

      //Now let's check for the parameters
      transformParameters( method );
    }


    //Transform the parameters for the constructors
    for ( CtConstructor constructor : ctClass.getConstructors() ) {
      transformParameters( constructor );
    }
  }

  private static void transformParameters( @Nonnull CtBehavior method ) throws ClassNotFoundException, NotFoundException, CannotCompileException {
    Object[][] parameterAnnotations = method.getParameterAnnotations();
    for ( int i = 0, parameterAnnotationsLength = parameterAnnotations.length; i < parameterAnnotationsLength; i++ ) {
      if ( AnnotationUtils.hasAnnotation( parameterAnnotations[i], Nonnull.class ) ) {
        //Skip primitive parameters
        if ( method.getParameterTypes()[i].isPrimitive() ) {
          continue;
        }

        int parameterNumber = i + 1;
        String format = MessageFormat.format( NON_NULL_PARAM, parameterNumber );
        insertAssertedVerificationCodeBefore( method, format );
      }
    }
  }
}