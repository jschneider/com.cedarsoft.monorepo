package com.cedarsoft.unit.other;

import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.quantity.Speed;
import com.cedarsoft.unit.si.SiDerivedUnit;
import com.cedarsoft.unit.si.ms;

/**
 * Pixels per milli second
 */
@Unit
@Speed
@SiDerivedUnit({px.class, ms.class})
@Name("pixels per millisecond")
@Symbol("px/ms")
public @interface px_ms {
}

