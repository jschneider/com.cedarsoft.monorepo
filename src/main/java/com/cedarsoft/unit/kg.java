package com.cedarsoft.unit;

import com.cedarsoft.quantity.Mass;
import com.cedarsoft.quantity.SIBaseUnit;

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

@Mass
@SIBaseUnit
@Symbol( "kg" )
@Name( "kilogram" )
public @interface kg {
}
