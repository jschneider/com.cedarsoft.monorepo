package com.cedarsoft.annotations.instrumentation;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cedarsoft.annotations.meta.ThreadDescribingAnnotation;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ThreadAnnotationTransformer extends AbstractAnnotationTransformer {
  @Nonnull
  private static String getThreadVerificationCode(@Nonnull List<? extends String> threadDescriptions) {
    StringBuilder paramsBuilder = new StringBuilder();

    paramsBuilder.append("new String[]{");

    for (Iterator<? extends String> iterator = threadDescriptions.iterator(); iterator.hasNext(); ) {
      String threadDescription = iterator.next();
      paramsBuilder.append("\"").append(threadDescription).append("\"");
      if (iterator.hasNext()) {
        paramsBuilder.append(",");
      }
    }
    paramsBuilder.append("}");

    return "com.cedarsoft.annotations.verification.VerifyThread.verifyThread(" + paramsBuilder + ");";
  }

  @Override
  protected void transformClass( @Nonnull CtClass ctClass ) throws ClassNotFoundException, CannotCompileException {
    for ( CtMethod method : ctClass.getMethods() ) {
      if ( method.isEmpty() ) {
        continue;
      }

      //Look for each annotation - are they thread related?
      List<String> threadDescriptions = new ArrayList<String>();

      for (Object annotation : method.getAnnotations()) {
        Class<? extends Annotation> annotationType = ((Annotation) annotation).annotationType();

        //Now find the annotation of the annotation
        @Nullable ThreadDescribingAnnotation threadDescribingAnnotation = AnnotationUtils.findAnnotation( annotationType.getAnnotations(), ThreadDescribingAnnotation.class );
        if (threadDescribingAnnotation == null) {
          continue;
        }

        threadDescriptions.add(threadDescribingAnnotation.value());
      }

      //Skip if no relevant annotations have been found
      if (threadDescriptions.isEmpty()) {
        continue;
      }

      insertAssertedVerificationCodeBefore(method, getThreadVerificationCode(threadDescriptions));
    }
  }
}
