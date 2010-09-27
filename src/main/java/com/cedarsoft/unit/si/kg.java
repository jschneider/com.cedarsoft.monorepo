package com.cedarsoft.unit.si;

import com.cedarsoft.quantity.Mass;
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

@Mass
@SIBaseUnit
@Symbol( "kg" )
@Name( "kilogram" )
public @interface kg {
}
