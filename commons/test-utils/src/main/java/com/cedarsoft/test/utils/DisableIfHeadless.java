package com.cedarsoft.test.utils;

import org.apiguardian.api.API;
import org.junit.jupiter.api.extension.*;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apiguardian.api.API.Status.STABLE;

/**
 * Disables tests when run headless
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@ExtendWith(DisableIfHeadlessCondition.class)
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = STABLE, since = "5.1")
public @interface DisableIfHeadless {
}
