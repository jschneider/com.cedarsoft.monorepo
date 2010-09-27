package com.cedarsoft.unit.other;

import com.cedarsoft.quantity.Angle;
import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.si.SiDerivedUnit;

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

@Angle
@Name( "degree" )
@Symbol( "°" )
public @interface deg {
}
