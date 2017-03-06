package com.cedarsoft.annotations;

import com.cedarsoft.annotations.meta.ThreadDescribingAnnotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated methods/classes must only be called from background/non-ui threads
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@ThreadDescribingAnnotation(NonUiThread.THREAD_DESCRIPTOR)
public @interface NonUiThread {
  String THREAD_DESCRIPTOR = "non-ui-thread";
}
