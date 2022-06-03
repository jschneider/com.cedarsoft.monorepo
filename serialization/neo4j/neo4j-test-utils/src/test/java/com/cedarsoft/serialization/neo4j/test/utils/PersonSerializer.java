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
package com.cedarsoft.serialization.neo4j.test.utils;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

import com.cedarsoft.serialization.neo4j.AbstractNeo4jSerializer;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionRange;

/**
 * The type Person serializer.
 */
public class PersonSerializer extends AbstractNeo4jSerializer<Person> {
  /**
   * Instantiates a new Person serializer.
   */
  public PersonSerializer() {
    super( "com.cedarsoft.test.person", VersionRange.single( 1, 0, 0 ) );

    getDelegatesMappings().add( new AddressSerializer() ).responsibleFor( Address.class )
      .map( 1, 0, 0 ).toDelegateVersion( 1, 0, 0 );
    getDelegatesMappings().add( new EmailSerializer() ).responsibleFor( Email.class )
      .map( 1, 0, 0 ).toDelegateVersion( 1, 0, 0 );


    getDelegatesMappings().verify();
  }

  @Override
  protected void serializeInternal(@Nonnull Node serializeTo, @Nonnull Person objectToSerialize, @Nonnull Version formatVersion) throws Exception {
    serializeTo.setProperty("name", objectToSerialize.getName());

    serializeWithRelationship(objectToSerialize.getAddress(), Address.class, serializeTo, Relations.ADDRESS, formatVersion);
    serializeWithRelationships(objectToSerialize.getMails(), Email.class, serializeTo, Relations.EMAIL, formatVersion);
  }

  @Nonnull
  @Override
  public Person deserialize(@Nonnull Node deserializeFrom, @Nonnull Version formatVersion) throws IOException {
    verifyVersionReadable(formatVersion);

    String name = (String) deserializeFrom.getProperty("name");

    Address address = deserializeWithRelationship(Address.class, Relations.ADDRESS, deserializeFrom, formatVersion);

    List<? extends Email> emails = deserializeWithRelationships(Email.class, Relations.EMAIL, deserializeFrom, formatVersion);
    return new Person(name, address, emails);
  }

  /**
   * The enum Relations.
   */
  public enum Relations implements RelationshipType {
    /**
     * The ADDRESS.
     */
    ADDRESS,
    /**
     * The EMAIL.
     */
    EMAIL
  }
}
