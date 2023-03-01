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

import java.io.IOException;

import javax.annotation.Nonnull;

import org.neo4j.graphdb.Node;

import it.neckar.open.serialization.neo4j.AbstractDelegatingNeo4jSerializer;
import it.neckar.open.serialization.neo4j.AbstractNeo4jSerializingStrategy;

import it.neckar.open.version.Version;
import it.neckar.open.version.VersionException;
import it.neckar.open.version.VersionRange;

/**
 */
public class BallSerializer extends AbstractDelegatingNeo4jSerializer<Ball> {
  public BallSerializer() {
    super( "http://test/ball", VersionRange.from( 1, 0, 0 ).to( 1, 1, 0 ) );

    addStrategy( new TennisBallSerializer() )
      .map( 1, 0, 0 ).toDelegateVersion( 1, 5, 0 )
      .map( 1, 1, 0 ).toDelegateVersion( 1, 5, 1 )
    ;

    addStrategy( new BasketBallSerializer() )
      .map( 1, 0, 0 ).toDelegateVersion( 2, 0, 0 )
      .map( 1, 1, 0 ).toDelegateVersion( 2, 0, 1 )
    ;

    getSerializingStrategySupport().verify();
  }

  /**
   *
   */
  public static class TennisBallSerializer extends AbstractNeo4jSerializingStrategy<Ball.TennisBall> {
    public TennisBallSerializer() {
      super( "tennisBall", "http://test/tennisball", Ball.TennisBall.class, VersionRange.from( 1, 5, 0 ).to( 1, 5, 1 ) );
    }


    @Override
    protected void serializeInternal(@Nonnull Node serializeTo, @Nonnull Ball.TennisBall objectToSerialize, @Nonnull Version formatVersion ) throws IOException {
      verifyVersionReadable( formatVersion );
      serializeTo.setProperty("newId", objectToSerialize.getId() );
    }

    @Nonnull
    @Override
    public Ball.TennisBall deserialize(@Nonnull Node deserializeFrom, @Nonnull Version formatVersion) throws IOException {
      verifyVersionReadable(formatVersion);

      int id;
      if (formatVersion.equals(Version.valueOf(1, 5, 0))) {
        //legacy support
        id = ((Number) deserializeFrom.getProperty("id")).intValue();
      }
      else {
        //This is the new version
        id = ((Number) deserializeFrom.getProperty("newId")).intValue();
      }

      return new Ball.TennisBall( id );
    }
  }

  /**
   *
   */
  public static class BasketBallSerializer extends AbstractNeo4jSerializingStrategy<Ball.BasketBall> {
    public BasketBallSerializer() {
      super( "basketBall", "http://test/basketball", Ball.BasketBall.class, VersionRange.from( 2, 0, 0 ).to( 2, 0, 1 ) );
    }

    @Override
    protected void serializeInternal(@Nonnull Node serializeTo, @Nonnull Ball.BasketBall objectToSerialize, @Nonnull Version formatVersion ) throws IOException {
      verifyVersionReadable( formatVersion );
      serializeTo.setProperty("myNewId", objectToSerialize.getTheId() );
    }

    @Nonnull
    @Override
    public Ball.BasketBall deserialize( @Nonnull Node deserializeFrom, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
      verifyVersionReadable( formatVersion );

      String id;
      if ( formatVersion.equals( Version.valueOf( 2, 0, 0 ) ) ) {
        //Old version
        id = ( String ) deserializeFrom.getProperty( "id" );
      }else{
        id = ( String ) deserializeFrom.getProperty( "myNewId" );
      }


      return new Ball.BasketBall(  id );
    }
  }
}
