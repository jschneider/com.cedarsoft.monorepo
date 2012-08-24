package com.cedarsoft.annotations.instrumentation;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UiThreadAnnotationTransformer implements ClassFileTransformer {
  @Nonnull
  private static CtClass getCtClass( @Nonnull final ClassLoader loader, @Nonnull String className ) throws NotFoundException {
    final ClassPool pool = ClassPool.getDefault();
    final LoaderClassPath lcp = new LoaderClassPath( loader );
    pool.appendClassPath( lcp );

    return pool.get( className );
  }

  private static boolean isAnnotated( @Nonnull CtMethod method, @Nonnull Class<? extends Annotation> annotationType ) throws ClassNotFoundException {
    for ( Object annotation : method.getAnnotations() ) {
      if ( annotationType.isAssignableFrom( annotation.getClass() ) ) {
        return true;
      }
    }

    return false;
  }

  @Override
  public byte[] transform( ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer ) throws IllegalClassFormatException {
    try {
      final CtClass ctClass = UiThreadAnnotationTransformer.getCtClass( loader, classBeingRedefined.getName() );
      ctClass.toBytecode();

      transformClass( ctClass );
      return ctClass.toBytecode();

    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private static void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException {
    for ( CtMethod method : ctClass.getMethods() ) {
      boolean uiThread = isAnnotated( method, UiThread.class );
      boolean nonUiThread = isAnnotated( method, NonUiThread.class );

      if ( uiThread && nonUiThread ) {
        throw new IllegalStateException( "Must not have both annotations in " + ctClass.getName() + "#" + method.getName() + ": " + UiThread.class.getName() + " and " + NonUiThread.class.getName() );
      }

      if ( uiThread ) {
        ctClass.defrost();
        insertUiThreadVerification( method );
      }
      if ( nonUiThread ) {
        ctClass.defrost();
        insertNonUiThreadVerification( method );
      }
    }
  }

  private static void insertUiThreadVerification( @Nonnull CtMethod method ) throws CannotCompileException {
    StringBuilder body = new StringBuilder();

    body.append( "{" );
    body.append( appendUiThreadVerificationCode() );
    body.append( "}" );

    method.insertBefore( body.toString() );
  }

  private static void insertNonUiThreadVerification( @Nonnull CtMethod method ) throws CannotCompileException {
    StringBuilder body = new StringBuilder();

    body.append( "{" );
    body.append( appendNonUiThreadVerificationCode() );
    body.append( "}" );

    method.insertBefore( body.toString() );
  }

  @Nonnull
  private static String appendUiThreadVerificationCode() {
    return "com.cedarsoft.annotations.verification.VerifyUiThread.verifyUiThreadAsserted();";
  }

  @Nonnull
  private static String appendNonUiThreadVerificationCode() {
    return "com.cedarsoft.annotations.verification.VerifyUiThread.verifyNonUiThreadAsserted();";
  }
}
