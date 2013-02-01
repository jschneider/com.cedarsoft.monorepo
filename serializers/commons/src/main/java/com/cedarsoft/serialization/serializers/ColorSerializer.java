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

package com.cedarsoft.serialization.serializers;

import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import com.cedarsoft.serialization.stax.mate.AbstractStaxMateSerializer;
import org.codehaus.staxmate.out.SMOutputElement;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.awt.Color;
import java.io.IOException;

public class ColorSerializer extends AbstractStaxMateSerializer<Color> {
  @Nonnull

  public static final String ELEMENT_RED = "red";
  @Nonnull

  public static final String ELEMENT_GREEN = "green";
  @Nonnull

  public static final String ELEMENT_BLUE = "blue";

  public ColorSerializer() {
    super( "color", "http://awt.java/color", VersionRange.from( 1, 0, 0 ).to( 1, 0, 0 ) );
  }

  @Override
  public void serialize( @Nonnull SMOutputElement serializeTo, @Nonnull Color object, @Nonnull Version formatVersion ) throws IOException, XMLStreamException {
    assert isVersionWritable( formatVersion );

    //red
    serializeTo.addElementWithCharacters( serializeTo.getNamespace(), ELEMENT_RED, String.valueOf( object.getRed() ) );
    //green
    serializeTo.addElementWithCharacters( serializeTo.getNamespace(), ELEMENT_GREEN, String.valueOf( object.getGreen() ) );
    //blue
    serializeTo.addElementWithCharacters( serializeTo.getNamespace(), ELEMENT_BLUE, String.valueOf( object.getBlue() ) );
  }

  @Nonnull
  @Override
  public Color deserialize( @Nonnull XMLStreamReader deserializeFrom, @Nonnull Version formatVersion ) throws VersionException, IOException, XMLStreamException {
    assert isVersionReadable( formatVersion );
    //red
    int red = Integer.parseInt( getChildText( deserializeFrom, ELEMENT_RED ) );
    //green
    int green = Integer.parseInt( getChildText( deserializeFrom, ELEMENT_GREEN ) );
    //blue
    int blue = Integer.parseInt( getChildText( deserializeFrom, ELEMENT_BLUE ) );
    //Finally closing element
    closeTag( deserializeFrom );
    //Constructing the deserialized object
    return new Color( red, green, blue );
  }

}