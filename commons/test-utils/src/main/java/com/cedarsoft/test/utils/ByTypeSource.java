package com.cedarsoft.test.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

import org.junit.jupiter.params.provider.*;

/**
 * Marks parameterized tests that are filled with methods/fields of a given type
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(ByTypeArgumentsProvider.class)
public @interface ByTypeSource {
  /**
   * The type of the field or return type of the method
   */
  @Nonnull
  Class<?> type();
}
