package com.cedarsoft.unit.si;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.prefix.micro;
import com.cedarsoft.unit.quantity.Time;

/**
 * micro seconds
 */
@Documented
@Inherited

@Unit
@Time
@Symbol(microS.SYMBOL)
@Name("microsecond")
@micro(s.class)

public @interface microS {
  String SYMBOL = "μs";
}
