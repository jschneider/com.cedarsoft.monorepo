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
package it.neckar.open.serialization.neo4j.test.utils;

import it.neckar.open.serialization.neo4j.AbstractNeo4jSerializer;

import it.neckar.open.version.Version;
import it.neckar.open.version.VersionException;
import it.neckar.open.version.VersionRange;

import org.neo4j.graphdb.Node;

import javax.annotation.Nonnull;

import java.io.IOException;

/**
*/
public class AddressSerializer extends AbstractNeo4jSerializer<Address> {
  /**
   * Instantiates a new Address serializer.
   */
  public AddressSerializer() {
    super("it.neckar.open.test.address", VersionRange.single(1, 0, 0));
  }

  @Override
  protected void serializeInternal(@Nonnull Node serializeTo, @Nonnull Address objectToSerialize, @Nonnull Version formatVersion ) {
    serializeTo.setProperty("street", objectToSerialize.getStreet() );
    serializeTo.setProperty("town", objectToSerialize.getTown() );
  }

  @Nonnull
  @Override
  public Address deserialize( @Nonnull Node deserializeFrom, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
    verifyVersionReadable( formatVersion );

    String street = ( String ) deserializeFrom.getProperty( "street" );
    String town = ( String ) deserializeFrom.getProperty( "town" );
    return new Address( street, town );
  }
}
