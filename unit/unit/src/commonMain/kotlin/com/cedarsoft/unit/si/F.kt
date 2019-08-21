/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cedarsoft.unit.si

import com.cedarsoft.unit.Unit
import com.cedarsoft.unit.quantity.ElectricCapacitance
import com.cedarsoft.unit.Definition
import com.cedarsoft.unit.Name
import com.cedarsoft.unit.Symbol

/**
 *
 */
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented

@Unit
@ElectricCapacitance
@Name("farad")
@SiDerivedUnit(A::class, V::class, s::class, J::class)
@Symbol(F.SYMBOL)
@Definition("A*s/V", "J/V²")
annotation class F {
  companion object {
    const val SYMBOL: String = "F"
  }
}
