package com.cedarsoft.test.utils;

import org.assertj.core.internal.cglib.core.*;
import org.junit.jupiter.api.extension.*;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(LocaleExtension.class)
public @interface WithLocale {
  @Nonnull
  String value();
}