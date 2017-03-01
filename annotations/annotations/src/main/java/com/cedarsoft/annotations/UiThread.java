package com.cedarsoft.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

import com.cedarsoft.annotations.meta.ThreadDescribingAnnotation;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( {ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.CONSTRUCTOR} )
@ThreadDescribingAnnotation(UiThread.THREAD_DESCRIPTOR)
public @interface UiThread {
  @Nonnull
  String THREAD_DESCRIPTOR = "ui-thread";
}
