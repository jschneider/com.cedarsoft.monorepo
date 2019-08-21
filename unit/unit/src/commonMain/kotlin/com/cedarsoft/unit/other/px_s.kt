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
package com.cedarsoft.unit.other

import com.cedarsoft.unit.Name
import com.cedarsoft.unit.Symbol
import com.cedarsoft.unit.Unit
import com.cedarsoft.unit.quantity.Speed
import com.cedarsoft.unit.si.SiDerivedUnit
import com.cedarsoft.unit.si.s

/**
 * Pixels per nanosecond
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */

@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Suppress("ClassName")
@Unit
@Speed
@SiDerivedUnit(px::class, s::class)
@Name("pixels per second")
@Symbol("px/s")
annotation class px_s

