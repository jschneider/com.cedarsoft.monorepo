package com.cedarsoft.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Documented
@Retention( RetentionPolicy.RUNTIME )
@Target( {ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.CONSTRUCTOR} )
public @interface UiThread {
}
