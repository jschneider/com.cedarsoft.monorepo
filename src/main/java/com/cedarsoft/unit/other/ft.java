package com.cedarsoft.unit.other;

import com.cedarsoft.quantity.Length;
import com.cedarsoft.unit.AlternativeSymbols;
import com.cedarsoft.unit.Definition;
import com.cedarsoft.unit.DerivedUnit;
import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.si.m;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Feet
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Length
@Name( "feet" )
@Symbol( "′" )
@AlternativeSymbols( "ft" )

@DerivedUnit( m.class )
@Definition( {"0.3048 m"} )
public @interface ft {

}
