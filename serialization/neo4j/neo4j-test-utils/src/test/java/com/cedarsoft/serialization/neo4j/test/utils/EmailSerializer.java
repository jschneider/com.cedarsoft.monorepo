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

import com.cedarsoft.serialization.neo4j.AbstractNeo4jSerializer;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import org.neo4j.graphdb.Node;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * The type Email serializer.
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class EmailSerializer extends AbstractNeo4jSerializer<Email> {
  /**
   * Instantiates a new Email serializer.
   */
  public EmailSerializer() {
    super( "com.cedarsoft.test.email", VersionRange.single( 1, 0, 0 ) );
  }

  @Override
  protected void serializeInternal( @Nonnull Node serializeTo, @Nonnull Email object, @Nonnull Version formatVersion ) {
    serializeTo.setProperty( "mail", object.getMail() );
  }

  @Nonnull
  @Override
  public Email deserialize( @Nonnull Node deserializeFrom, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
    verifyVersionReadable( formatVersion );

    String mail = ( String ) deserializeFrom.getProperty( "mail" );
    return new Email( mail );
  }
}
