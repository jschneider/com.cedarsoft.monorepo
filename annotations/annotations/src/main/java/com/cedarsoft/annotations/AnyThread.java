package com.cedarsoft.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

import com.cedarsoft.annotations.meta.ThreadDescribingAnnotation;

/**
 * Methods annotated with this annotation can be called from any thread (UI thread or not).
 * They return fast.
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.CONSTRUCTOR})
@ThreadDescribingAnnotation(AnyThread.THREAD_DESCRIPTOR)
public @interface AnyThread {
  @Nonnull
  String THREAD_DESCRIPTOR = "any-thread";
}
