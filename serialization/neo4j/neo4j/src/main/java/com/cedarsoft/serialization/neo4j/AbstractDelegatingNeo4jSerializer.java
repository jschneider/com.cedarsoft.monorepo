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
package com.cedarsoft.serialization.neo4j;

import com.cedarsoft.serialization.SerializationException;
import com.cedarsoft.serialization.SerializingStrategy;
import com.cedarsoft.serialization.SerializingStrategySupport;
import com.cedarsoft.serialization.VersionMapping;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Collection;

/**
 * Abstract base class for all neo4j serializers
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class AbstractDelegatingNeo4jSerializer<T> extends AbstractNeo4jSerializer<T> {
  @Nonnull
  public static final String PROPERTY_DELEGATING_FORMAT_VERSION = "delegatingFormatVersion";
  /**
   * Used for delegating serializers
   */
  @Nonnull
  public static final String PROPERTY_SUB_TYPE = "subtype";

  @Nonnull
  protected final SerializingStrategySupport<T, Node, Node, IOException, Node, Node> serializingStrategySupport;

  protected AbstractDelegatingNeo4jSerializer( @Nonnull String nameSpaceUriBase, @Nonnull VersionRange formatVersionRange ) {
    super( nameSpaceUriBase, formatVersionRange );
    this.serializingStrategySupport = new SerializingStrategySupport<T, Node, Node, IOException, Node, Node>( formatVersionRange );
  }

  /** @noinspection RefusedBequest*/
  @Override
  protected void addVersion( @Nonnull Node serializeTo ) {
    serializeTo.setProperty( PROPERTY_DELEGATING_FORMAT_VERSION, getFormatVersion().toString() );
  }

  /** @noinspection RefusedBequest*/
  @Nonnull
  @Override
  protected Version readVersion( @Nonnull Node in ) {
    return Version.parse( ( String ) in.getProperty( PROPERTY_DELEGATING_FORMAT_VERSION ) );
  }

  @Override
  protected void serializeInternal( @Nonnull Node serializeTo, @Nonnull T object, @Nonnull Version formatVersion ) throws IOException {
    assert isVersionWritable( formatVersion );

    SerializingStrategy<T, Node, Node, IOException, Node, Node> strategy = serializingStrategySupport.findStrategy( object );
    Version resolvedVersion = serializingStrategySupport.resolveVersion( strategy, formatVersion );

    serializeTo.setProperty( PROPERTY_SUB_TYPE, strategy.getId() );

    strategy.serialize( serializeTo, object, resolvedVersion );
  }

  @Nonnull
  @Override
  public T deserialize( @Nonnull Node deserializeFrom, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
    assert isVersionReadable( formatVersion );

    try {
      @Nonnull String subType = ( String ) deserializeFrom.getProperty( PROPERTY_SUB_TYPE );
      SerializingStrategy<? extends T, Node, Node, IOException, Node, Node> strategy = serializingStrategySupport.findStrategy( subType );
      Version resolvedVersion = serializingStrategySupport.resolveVersion( strategy, formatVersion );
      return strategy.deserialize( deserializeFrom, resolvedVersion );

    } catch ( NotFoundException e ) {
      throw new SerializationException( e, PROPERTY_SUB_TYPE, SerializationException.Details.INVALID_NODE, deserializeFrom.getId(), deserializeFrom.getPropertyKeys() );
    }
  }

  @Nonnull
  public Collection<? extends SerializingStrategy<? extends T, Node, Node, IOException, Node, Node>> getStrategies() {
    return serializingStrategySupport.getStrategies();
  }

  @Nonnull
  public VersionMapping addStrategy( @Nonnull SerializingStrategy<? extends T, Node, Node, IOException, Node, Node> strategy ) {
    return serializingStrategySupport.addStrategy( strategy );
  }

  @Nonnull
  public SerializingStrategySupport<T, Node, Node, IOException, Node, Node> getSerializingStrategySupport() {
    return serializingStrategySupport;
  }
}
