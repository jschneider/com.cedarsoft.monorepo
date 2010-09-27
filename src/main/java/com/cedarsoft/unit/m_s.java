package com.cedarsoft.unit;

import com.cedarsoft.quantity.SiDerivedUnit;
import com.cedarsoft.quantity.Speed;

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

@Speed
@SiDerivedUnit
@Name( "metre per second" )
@Symbol( "m/s" )
public @interface m_s {
}
