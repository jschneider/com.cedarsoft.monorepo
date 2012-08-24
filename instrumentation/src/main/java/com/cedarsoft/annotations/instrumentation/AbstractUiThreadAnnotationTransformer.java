package com.cedarsoft.annotations.instrumentation;

import com.cedarsoft.annotations.UiThread;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import javax.annotation.Nonnull;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractUiThreadAnnotationTransformer implements ClassFileTransformer {
  @Nonnull
  private static CtClass getCtClass( @Nonnull final ClassLoader loader, @Nonnull String className ) throws NotFoundException {
    final ClassPool pool = ClassPool.getDefault();
    final LoaderClassPath lcp = new LoaderClassPath( loader );
    pool.appendClassPath( lcp );

    return pool.get( className );
  }

  @Override
  public byte[] transform( ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer ) throws IllegalClassFormatException {
    System.out.println( "---------------------" );
    System.out.println( "UiThreadAnnotationTransformer.transform: " + classBeingRedefined.getName() );

    try {
      final CtClass ctClass = getCtClass( loader, classBeingRedefined.getName() );
      ctClass.toBytecode();

      transformClass( ctClass );
      return ctClass.toBytecode();

    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException, NotFoundException {
    for ( CtMethod method : ctClass.getMethods() ) {
      if ( !isAnnotated( method, UiThread.class ) ) {
        continue;
      }

      ctClass.defrost();
      wrap( ctClass, method );
    }
  }

  private void wrap( @Nonnull CtClass clas, @Nonnull CtMethod methodOld ) throws CannotCompileException, NotFoundException {
    StringBuilder body = new StringBuilder();

    body.append( "{" );
    body.append( appendVerificationCode() );
    body.append( "}" );

    methodOld.insertBefore( body.toString() );
  }

  @Nonnull
  protected abstract String appendVerificationCode();

  private static boolean isAnnotated( @Nonnull CtMethod method, @Nonnull Class<UiThread> annotationType ) throws ClassNotFoundException {
    for ( Object annotation : method.getAnnotations() ) {
      System.out.println( "Annotation: " + annotation );
      System.out.println( "Annotation: " + annotation.getClass().getName() );

      if ( annotationType.isAssignableFrom( annotation.getClass() ) ) {
        return true;
      }
    }

    return false;
  }
}
