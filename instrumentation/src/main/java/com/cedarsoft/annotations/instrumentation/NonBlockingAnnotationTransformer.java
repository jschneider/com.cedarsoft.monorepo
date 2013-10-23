package com.cedarsoft.annotations.instrumentation;

import javax.annotation.Nonnull;

import com.cedarsoft.annotations.NonBlocking;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NonBlockingAnnotationTransformer extends AbstractAnnotationTransformer {
  @Override
  protected void transformClass(@Nonnull CtClass ctClass) throws ClassNotFoundException, CannotCompileException {
    for (CtMethod method : ctClass.getMethods()) {
      if (method.isEmpty()) {
        continue;
      }

      if (!AnnotationUtils.hasAnnotation(method.getAnnotations(), NonBlocking.class)) {
        continue;
      }

      try {
        method.addLocalVariable("notStuckVerifier", ctClass.getClassPool().get("com.cedarsoft.annotations.verification.NotStuckVerifier"));
        method.insertBefore(wrapInAssertion(ctClass, "notStuckVerifier = com.cedarsoft.annotations.verification.NotStuckVerifier.start();"));
        method.insertBefore("notStuckVerifier=null;");
        method.insertAfter(wrapInAssertion(ctClass, "if (notStuckVerifier!=null) {notStuckVerifier.finished();}"));
      } catch (NotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
