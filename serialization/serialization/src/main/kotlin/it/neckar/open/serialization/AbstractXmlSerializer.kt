/**
 * Copyright (C) cedarsoft GmbH.

 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at

 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)

 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.

 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).

 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.

 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package it.neckar.open.serialization

import it.neckar.open.version.VersionRange

/**
 * Abstract base class for xml based serializers.
 * The version information is stored within the namespace declaration of the root element
 * @param <T> the type of object this serializer is able to (de)serialize
 * @param <S> the object to serialize to
 * @param <D> the object to deserialize from
 */
abstract class AbstractXmlSerializer<T : Any, S : Any, D : Any>
/**
 * Creates a new serializer
 * @param defaultElementName the default element name
 * @param nameSpaceBase      the base for the namespace uri
 * @param formatVersionRange the version range. The max value is used when written.
 */
protected constructor(
  val defaultElementName: String, nameSpaceBase: String, formatVersionRange: VersionRange
) : AbstractNameSpaceBasedSerializer<T, S, D>(nameSpaceBase, formatVersionRange) {

  override fun <ST : Any> getSerializer(type: Class<ST>): AbstractXmlSerializer<in ST, S, D> {
    return super.getSerializer(type) as AbstractXmlSerializer<in ST, S, D>
  }
}
