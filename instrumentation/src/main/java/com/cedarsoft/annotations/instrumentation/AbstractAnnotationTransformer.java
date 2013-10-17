package com.cedarsoft.annotations.instrumentation;

import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javax.annotation.Nonnull;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractAnnotationTransformer implements ClassFileTransformer {
  @Override
  public byte[] transform( @Nonnull ClassLoader loader, @Nonnull String className, @Nonnull Class<?> classBeingRedefined, @Nonnull ProtectionDomain protectionDomain, @Nonnull byte[] classfileBuffer ) throws IllegalClassFormatException {
    try {
      final CtClass ctClass = AbstractAnnotationTransformer.getCtClass( loader, classBeingRedefined.getName() );

      if (ctClass.isInterface()) {
        return classfileBuffer;
      }

      transformClass( ctClass );
      return ctClass.toBytecode();

    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  protected abstract void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException, NotFoundException;

  protected static boolean isAnnotated( @Nonnull CtMethod method, @Nonnull Class<? extends Annotation> annotationType ) throws ClassNotFoundException {
    return method.hasAnnotation(annotationType);
  }

  @Nonnull
  protected static CtClass getCtClass( @Nonnull final ClassLoader loader, @Nonnull String className ) throws NotFoundException {
    final ClassPool pool = new ClassPool(true);
    pool.appendClassPath(new LoaderClassPath(loader));

    return pool.get(className);
  }

  protected static void insertAssertedVerificationCodeBefore( @Nonnull CtBehavior method, @Nonnull String verificationCode ) throws CannotCompileException {
    method.insertBefore( wrapInAssertion( method.getDeclaringClass(), verificationCode ) );
  }

  protected static void insertAssertedVerificationCodeAfter( @Nonnull CtMethod method, @Nonnull String verificationCode ) throws CannotCompileException {
    method.insertAfter( wrapInAssertion( method.getDeclaringClass(), verificationCode ) );
  }

  /**
   * Wraps the given code into an assertion statement
   *
   * @param declaringClass the declaring class
   * @param code the assertion code
   * @return the assert statement
   */
  @Nonnull
  protected static String wrapInAssertion(@Nonnull CtClass declaringClass, @Nonnull String code) {
    return "if( " + declaringClass.getName() + ".class.desiredAssertionStatus() ){" + code + "}";
  }
}
