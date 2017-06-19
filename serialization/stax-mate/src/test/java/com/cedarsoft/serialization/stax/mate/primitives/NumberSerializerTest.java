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
package com.cedarsoft.serialization.stax.mate.primitives;

import com.cedarsoft.serialization.StreamSerializer;
import com.cedarsoft.serialization.test.utils.AbstractXmlSerializerTest2;
import com.cedarsoft.serialization.test.utils.Entry;
import org.junit.*;
import org.junit.experimental.theories.*;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NumberSerializerTest extends AbstractXmlSerializerTest2<Number> {
  @Nonnull
  @Override
  protected StreamSerializer<Number> getSerializer() throws Exception {
    return new NumberSerializer();
  }

  @Test
  public void testNotClose() throws Exception {
    final boolean[] shallAcceptClose = {false};

    OutputStream out = new FilterOutputStream( new ByteArrayOutputStream() ) {
      private boolean closed;

      @Override
      public void close() throws IOException {
        if ( !shallAcceptClose[0] ) {
          fail( "Unacceptable close!" );
        }

        super.close();
        closed = true;
      }
    };

    getSerializer().serialize( 123.0, out );
    shallAcceptClose[0] = true;
    out.close();
  }

  @Override
  protected void verifyDeserialized( @Nonnull Number deserialized, @Nonnull Number original ) {
    assertThat( deserialized.doubleValue() ).isEqualTo( original.doubleValue() );
    assertThat( deserialized.intValue() ).isEqualTo( original.intValue() );
  }

  @DataPoint
  public static final Entry<?> ENTRY1 = create( 123, "<number>123</number>" );
  @DataPoint
  public static final Entry<?> ENTRY2 = create( 123.5, "<number>123.5</number>" );
  @DataPoint
  public static final Entry<?> ENTRY3 = create( -123.5, "<number>-123.5</number>" );

  @DataPoint
  public static final Entry<?> ENTRY4 = create( Double.MAX_VALUE, "<number>1.7976931348623157E308</number>" );
  @DataPoint
  public static final Entry<?> ENTRY5 = create( -Double.MAX_VALUE, "<number>-1.7976931348623157E308</number>" );
}