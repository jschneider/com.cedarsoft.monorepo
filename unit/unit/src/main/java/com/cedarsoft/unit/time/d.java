package com.cedarsoft.unit.time;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.other.Factor;
import com.cedarsoft.unit.quantity.Time;
import com.cedarsoft.unit.si.s;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited

@Unit
@Time
@Symbol(d.SYMBOL)
@Name("Day")
@Factor(factor = 60 * 60 * 24, base = s.class)
public @interface d {
  String SYMBOL = "d";
}

