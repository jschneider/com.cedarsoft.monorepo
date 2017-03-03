package com.cedarsoft.unit.si;

import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.prefix.milli;
import com.cedarsoft.unit.quantity.Time;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Unit
@Time
@Symbol( ms.SYMBOL )
@Name( "milliseconds" )
@milli( s.class )
public @interface ms {
  String SYMBOL = "ms";
}

