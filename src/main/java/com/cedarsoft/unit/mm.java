package com.cedarsoft.unit;

import com.cedarsoft.quantity.Length;
import com.cedarsoft.quantity.SiDerivedUnit;
import com.cedarsoft.unit.prefix.milli;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @noinspection AnnotationNamingConvention
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Length
@SiDerivedUnit
@Symbol( "mm" )
@Name( "millimetre" )
@milli
public @interface mm {
}
