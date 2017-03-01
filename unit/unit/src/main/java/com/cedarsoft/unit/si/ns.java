package com.cedarsoft.unit.si;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.prefix.milli;
import com.cedarsoft.unit.prefix.nano;
import com.cedarsoft.unit.quantity.Time;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Unit
@Time
@Symbol( ns.SYMBOL )
@Name( "nanoseconds" )
@nano( s.class )
public @interface ns {
  String SYMBOL = "ns";
}

