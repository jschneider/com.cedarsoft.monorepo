package com.cedarsoft.annotations.instrumentation;

import com.cedarsoft.annotations.NonUiThread;
import com.cedarsoft.annotations.UiThread;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.DuplicateMemberException;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class UiThreadAnnotationTransformer implements ClassFileTransformer {
  @Override
  public byte[] transform( @Nonnull ClassLoader loader, @Nonnull String className, @Nonnull Class<?> classBeingRedefined, @Nonnull ProtectionDomain protectionDomain, @Nonnull byte[] classfileBuffer ) throws IllegalClassFormatException {
    try {
      final CtClass ctClass = UiThreadAnnotationTransformer.getCtClass( loader, classBeingRedefined.getName() );

      if (ctClass.isInterface()) {
        return classfileBuffer;
      }

      transformClass( ctClass );
      return ctClass.toBytecode();

    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  private static CtClass getCtClass( @Nonnull final ClassLoader loader, @Nonnull String className ) throws NotFoundException {
    final ClassPool pool = new ClassPool(true);
    pool.appendClassPath(new LoaderClassPath(loader));

    return pool.get(className);
  }

  private static boolean isAnnotated( @Nonnull CtMethod method, @Nonnull Class<? extends Annotation> annotationType ) throws ClassNotFoundException {
    for ( Object annotation : method.getAvailableAnnotations() ) {
      if ( annotationType.isAssignableFrom( annotation.getClass() ) ) {
        return true;
      }
    }

    return false;
  }

  private static void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException {
    for ( CtMethod method : ctClass.getMethods() ) {
      if (method.isEmpty()) {
        continue;
      }

      boolean uiThread = isAnnotated( method, UiThread.class );
      boolean nonUiThread = isAnnotated( method, NonUiThread.class );

      if ( uiThread && nonUiThread ) {
        throw new IllegalStateException( "Must not have both annotations in " + ctClass.getName() + "#" + method.getName() + ": " + UiThread.class.getName() + " and " + NonUiThread.class.getName() );
      }

      if ( uiThread ) {
        insertAssertedVerificationCode(method, getUiThreadVerificationCode());
      }
      if ( nonUiThread ) {
        insertAssertedVerificationCode(method, getNonUiThreadVerificationCode());
      }
    }
  }

  private static void insertAssertedVerificationCode(@Nonnull CtMethod method, @Nonnull String verificationCode) throws CannotCompileException {
    ensureAssertField(method.getDeclaringClass());

    StringBuilder body = new StringBuilder();

    body.append("if( !" + ASSERTION_DISABLED_FIELD_NAME + " ){");
    body.append(verificationCode);
    body.append("}");

    method.insertBefore(body.toString());
  }

  @Nonnull
  private static String getUiThreadVerificationCode() {
    return "com.cedarsoft.annotations.verification.VerifyUiThread.verifyUiThread();";
  }

  @Nonnull
  private static String getNonUiThreadVerificationCode() {
    return "com.cedarsoft.annotations.verification.VerifyUiThread.verifyNonUiThread();";
  }

  @Nonnull
  public static final String ASSERTION_DISABLED_FIELD_NAME = "$assertionsDisabled";

  /**
   * Ensures that the assert field for the class exists
   *
   * @param ctClass the class
   */
  private static void ensureAssertField(@Nonnull CtClass ctClass) throws CannotCompileException {
    try {
      CtField assertionsDisabledField = CtField.make("static final boolean " + ASSERTION_DISABLED_FIELD_NAME + " = !" + ctClass.getName() + ".class.desiredAssertionStatus();", ctClass);
      ctClass.addField(assertionsDisabledField);
    } catch (DuplicateMemberException ignore) {
    }
  }
}