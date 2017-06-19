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
package com.cedarsoft.unit.si;

import com.cedarsoft.unit.Unit;
import com.cedarsoft.unit.quantity.ElectricResistance;
import com.cedarsoft.unit.Definition;
import com.cedarsoft.unit.Name;
import com.cedarsoft.unit.Symbol;
import com.cedarsoft.unit.quantity.ElectricResistance;

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
@ElectricResistance
@Name( "ohm" )
@SiDerivedUnit( {A.class, V.class} )
@Symbol( ohm.SYMBOL )
@Definition( {"V/A", "J/s*A²"} )
public @interface ohm {
  String SYMBOL = "Ω";

}
