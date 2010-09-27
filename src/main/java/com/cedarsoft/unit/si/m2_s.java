package com.cedarsoft.unit.si;

import com.cedarsoft.quantity.Acceleration;
import com.cedarsoft.quantity.Speed;
import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Represents a metre
 *
 * @noinspection AnnotationNamingConvention
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Acceleration
@SiDerivedUnit
@Name( "square metre per second" )
@Symbol( "m²/s" )
public @interface m2_s {
}
