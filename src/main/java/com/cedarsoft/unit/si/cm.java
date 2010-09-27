package com.cedarsoft.unit.si;

import com.cedarsoft.quantity.Length;
import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.prefix.centi;

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
@Symbol( "cm" )
@Name( "centimetre" )
@centi
public @interface cm {
}
