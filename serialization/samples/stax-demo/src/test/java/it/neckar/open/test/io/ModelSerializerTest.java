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

package it.neckar.open.test.io;

import javax.annotation.Nonnull;

import it.neckar.open.serialization.StreamSerializer;
import it.neckar.open.serialization.test.utils.AbstractXmlSerializerTest2;
import it.neckar.open.serialization.test.utils.Entry;

import it.neckar.open.test.Model;

/**
 *
 */
public class ModelSerializerTest extends AbstractXmlSerializerTest2<Model> {
  @Nonnull
  @Override
  protected StreamSerializer<Model> getSerializer() {
    //We create a serializer. This one is very easy. But sometimes it needs a little bit of work...
    return new ModelSerializer();
  }

  //Just create a few examples of objects that shall be serialized
  public static final Entry<? extends Model> ENTRY1 = create(
    new Model("Toyota"),

    //We just return the sole part of the xml that should be compared.
    //For comparison XML-Unit is used, so there is no need to take care of formatting etc.

    //Note: The xml serializers write a version information to the xml. This has been left out here!
    "<model>Toyota</model>");
  public static final Entry<? extends Model> ENTRY2 = create(
    new Model("GM"), "<model>GM</model>");
  public static final Entry<? extends Model> ENTRY3 = create(
    new Model("Volkswagen"), "<model>Volkswagen</model>");
  public static final Entry<? extends Model> ENTRY4 = create(
    new Model("Renault"), "<model>Renault</model>");

  @Override
  protected void verifyDeserialized(@Nonnull Model deserialized, @Nonnull Model original) {
    //We *might* override this method and verify the deserialized objects on our own
    //The default implementation simply calls "equals" for each single object.
    super.verifyDeserialized(deserialized, original);
  }
}
