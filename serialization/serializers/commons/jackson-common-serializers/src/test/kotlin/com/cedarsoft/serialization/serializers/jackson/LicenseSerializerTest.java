/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package it.neckar.open.serialization.serializers.jackson;

import javax.annotation.Nonnull;

import it.neckar.open.license.License;

import it.neckar.open.serialization.StreamSerializer;
import it.neckar.open.serialization.test.utils.AbstractJsonSerializerTest2;
import it.neckar.open.serialization.test.utils.AbstractSerializerTest2;
import it.neckar.open.serialization.test.utils.Entry;

public class LicenseSerializerTest extends AbstractJsonSerializerTest2<License> {
  @Nonnull
  public static final Entry<? extends License> ENTRY1 = AbstractSerializerTest2.create( License.GPL_3, LicenseSerializerTest.class.getResource( "License_1.0.0_1.json" ) );
  public static final Entry<? extends License> ENTRY_NULL_URL = LicenseSerializerTest.create(
    new License( "daId", "daName" ), LicenseSerializerTest.class.getResource( "License_1.0.0_nullUrl.json" ) );

  public static final Entry<? extends License> ENTRY_CC = LicenseSerializerTest.create(
    License.CC_BY_NC_SA, LicenseSerializerTest.class.getResource( "License_1.0.0_CC.json" ) );

  @Nonnull
  @Override
  protected StreamSerializer<License> getSerializer() throws Exception {
    return new LicenseSerializer();
  }
}
