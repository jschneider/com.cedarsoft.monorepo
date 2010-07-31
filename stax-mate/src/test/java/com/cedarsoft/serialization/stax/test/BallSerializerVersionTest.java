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

package com.cedarsoft.serialization.stax.test;

import com.cedarsoft.Version;
import com.cedarsoft.serialization.AbstractXmlVersionTest2;
import com.cedarsoft.serialization.Serializer;
import com.cedarsoft.serialization.VersionEntry;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.theories.*;

import static org.junit.Assert.*;

/**
 *
 */
public class BallSerializerVersionTest extends AbstractXmlVersionTest2<Ball> {
  @NotNull
  @Override
  protected Serializer<Ball> getSerializer() throws Exception {
    return new BallSerializer();
  }

  @Override
  protected void verifyDeserialized( @NotNull Ball deserialized, @NotNull Version version ) throws Exception {
    if ( deserialized instanceof Ball.BasketBall ) {
      assertEquals( "asdf", ( ( Ball.BasketBall ) deserialized ).getTheId() );
    }

    if ( deserialized instanceof Ball.TennisBall ) {
      assertEquals( 7, ( ( Ball.TennisBall ) deserialized ).getId() );
    }
  }

  @DataPoint
  public static final VersionEntry ENTRY1 = create( Version.valueOf( 1, 0, 0 ), "<ball type=\"basketBall\">asdf</ball>" );
  @DataPoint
  public static final VersionEntry ENTRY4 = create( Version.valueOf( 1, 0, 0 ), "<ball type=\"tennisBall\">7</ball>" );

  @DataPoint
  public static final VersionEntry ENTRY2 = create( Version.valueOf( 1, 1, 0 ), "<ball type=\"tennisBall\" id=\"7\" />" );
  @DataPoint
  public static final VersionEntry ENTRY3 = create( Version.valueOf( 1, 1, 0 ), "<ball type=\"basketBall\" theId=\"asdf\"/>" );
}
