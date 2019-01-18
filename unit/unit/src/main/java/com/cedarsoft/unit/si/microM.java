package com.cedarsoft.unit.si;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.prefix.micro;
import com.cedarsoft.unit.quantity.Length;

/**
 * Micrometer
 */
@Documented
@Inherited

@Unit
@Length
@Symbol(microM.SYMBOL)
@Name("micrometer")
@micro(m.class)

public @interface microM {
  String SYMBOL = "μm";
}
