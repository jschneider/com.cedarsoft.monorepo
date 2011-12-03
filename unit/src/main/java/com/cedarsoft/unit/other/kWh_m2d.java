/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cedarsoft.unit.other;

import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.quantity.Intensity;
import com.cedarsoft.unit.Definition;
import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.quantity.Intensity;
import com.cedarsoft.unit.si.SiDerivedUnit;
import com.cedarsoft.unit.si.kWh;
import com.cedarsoft.unit.si.s;

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

@Unit
@Intensity
@Name( "kWh per square meter and day" )
@SiDerivedUnit( {kWh.class, s.class} )
@Symbol( "kW*h/(m²*day" )
@Definition( {"kW*h/(m²*day"} )
public @interface kWh_m2d {
}
