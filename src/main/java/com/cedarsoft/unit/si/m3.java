package com.cedarsoft.unit.si;

import com.cedarsoft.quantity.Volume;
import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * A square metre
 *
 * @noinspection AnnotationNamingConvention
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Volume
@SiDerivedUnit
@Name( "cubic metre" )
@Symbol( "m³" )
public @interface m3 {
}
