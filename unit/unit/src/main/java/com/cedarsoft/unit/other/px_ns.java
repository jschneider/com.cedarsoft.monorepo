package com.cedarsoft.unit.other;

import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.quantity.Speed;
import com.cedarsoft.unit.si.SiDerivedUnit;
import com.cedarsoft.unit.si.ns;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Pixels per nanosecond
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@Unit
@Speed
@SiDerivedUnit({px.class, ns.class})
@Name("pixels per nanosecond")
@Symbol("px/ns")
public @interface px_ns {
}

