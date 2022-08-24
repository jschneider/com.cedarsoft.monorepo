/**
 * Copyright (C) cedarsoft GmbH.
 * <p>
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 * <p>
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 * <p>
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * <p>
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.serialization.neo4j.test.utils;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.neo4j.graphdb.Node;

import com.cedarsoft.serialization.neo4j.AbstractDelegatingNeo4jSerializer;
import com.cedarsoft.serialization.neo4j.AbstractNeo4jSerializingStrategy;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionRange;

/**
 */
public class BallSerializerOld extends AbstractDelegatingNeo4jSerializer<Ball> {
  public BallSerializerOld() {
    super("http://test/ball", VersionRange.single(1, 0, 0));

    addStrategy(new TennisBallSerializer())
      .map(1, 0, 0).toDelegateVersion(1, 0, 0)
    ;

    addStrategy(new BasketBallSerializer())
      .map(1, 0, 0).toDelegateVersion(1, 0, 0)
    ;

    getSerializingStrategySupport().verify();
  }

  /**
   *
   */
  public static class TennisBallSerializer extends AbstractNeo4jSerializingStrategy<Ball.TennisBall> {
    public TennisBallSerializer() {
      super("tennisBall", "http://test/tennisball", Ball.TennisBall.class, VersionRange.single(1, 0, 0));
    }


    @Override
    protected void serializeInternal(@Nonnull Node serializeTo, @Nonnull Ball.TennisBall objectToSerialize, @Nonnull Version formatVersion) {
      verifyVersionReadable(formatVersion);
      serializeTo.setProperty("id", objectToSerialize.getId());
    }

    @Nonnull
    @Override
    public Ball.TennisBall deserialize(@Nonnull Node deserializeFrom, @Nonnull Version formatVersion) {
      verifyVersionReadable(formatVersion);

      int id = ((Number) deserializeFrom.getProperty("id")).intValue();
      return new Ball.TennisBall(id);
    }
  }

  /**
   *
   */
  public static class BasketBallSerializer extends AbstractNeo4jSerializingStrategy<Ball.BasketBall> {
    public BasketBallSerializer() {
      super("basketBall", "http://test/basketball", Ball.BasketBall.class, VersionRange.single(1, 0, 0));
    }

    @Override
    protected void serializeInternal(@Nonnull Node serializeTo, @Nonnull Ball.BasketBall objectToSerialize, @Nonnull Version formatVersion) throws IOException {
      verifyVersionReadable(formatVersion);
      serializeTo.setProperty("id", objectToSerialize.getTheId());
    }

    @Nonnull
    @Override
    public Ball.BasketBall deserialize(@Nonnull Node deserializeFrom, @Nonnull Version formatVersion) {
      verifyVersionReadable(formatVersion);

      String id = (String) deserializeFrom.getProperty("id");
      return new Ball.BasketBall(id);
    }
  }
}
