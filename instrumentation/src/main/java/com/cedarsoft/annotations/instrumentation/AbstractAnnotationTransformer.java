package com.cedarsoft.annotations.instrumentation;

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

  @Nonnull
  public static final String ASSERTION_DISABLED_FIELD_NAME = "$assertionsDisabled";

  protected abstract void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException ;

  protected static boolean isAnnotated( @Nonnull CtMethod method, @Nonnull Class<? extends Annotation> annotationType ) throws ClassNotFoundException {
    return method.hasAnnotation(annotationType);
  }

  @Nonnull
  protected static CtClass getCtClass( @Nonnull final ClassLoader loader, @Nonnull String className ) throws NotFoundException {
    final ClassPool pool = new ClassPool(true);
    pool.appendClassPath(new LoaderClassPath(loader));

    return pool.get(className);
  }

  protected static void insertAssertedVerificationCodeBefore( @Nonnull CtMethod method, @Nonnull String verificationCode ) throws CannotCompileException {
    ensureAssertField( method.getDeclaringClass() );
    method.insertBefore( wrapInAssertion( verificationCode ) );
  }

  protected static void insertAssertedVerificationCodeAfter( @Nonnull CtMethod method, @Nonnull String verificationCode ) throws CannotCompileException {
    ensureAssertField(method.getDeclaringClass());
    method.insertAfter( wrapInAssertion( verificationCode ) );
  }

  /**
   * Wraps the given code into an assertion statement
   * @param code the assertion code
   * @return the assert statement
   */
  @Nonnull
  protected static String wrapInAssertion( @Nonnull String code ) {
    StringBuilder body = new StringBuilder();

    body.append( "if( !" + ASSERTION_DISABLED_FIELD_NAME + " ){" );
    body.append(code);
    body.append("}");
    return body.toString();
  }

  /**
   * Ensures that the assert field for the class exists
   *
   * @param ctClass the class
   */
  protected static void ensureAssertField( @Nonnull CtClass ctClass ) throws CannotCompileException {
    try {
      CtField assertionsDisabledField = CtField.make("static final boolean " + ASSERTION_DISABLED_FIELD_NAME + " = !" + ctClass.getName() + ".class.desiredAssertionStatus();", ctClass);
      ctClass.addField(assertionsDisabledField);
    } catch (DuplicateMemberException ignore) {
    }
  }
}
