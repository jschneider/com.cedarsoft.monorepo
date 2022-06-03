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
package com.cedarsoft.serialization.jackson.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.cedarsoft.serialization.jackson.JacksonParserWrapper;
import com.cedarsoft.test.utils.MockitoTemplate;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 */
public class FilteringJsonParserTest {
  @Test
  public void testListener() throws Exception {
    final Filter filter = new Filter() {
      @Override
      public boolean shallFilterOut( @Nonnull JsonParser parser ) throws IOException, JsonParseException {
        return parser.getCurrentName().startsWith( "_" );
      }
    };

    new MockitoTemplate() {
      @Mock
      private FilteredParserListener listener;

      private final InputStream in = getClass().getResourceAsStream( "filter.json" );
      private final FilteringJsonParser parser = new FilteringJsonParser( new JsonFactory().createParser( in ), filter );
      private final JacksonParserWrapper wrapper = new JacksonParserWrapper( parser );

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        parser.addListener( listener );

        try {
          wrapper.nextToken( JsonToken.START_OBJECT );
          wrapper.nextField( "id" );
          wrapper.nextToken( JsonToken.VALUE_STRING );

          wrapper.nextField( "key" );
          wrapper.nextToken( JsonToken.START_OBJECT );
          wrapper.nextField( "description" );
          wrapper.nextToken( JsonToken.VALUE_STRING );
          wrapper.nextToken( JsonToken.END_OBJECT );

          wrapper.nextField( "value" );
          wrapper.nextToken( JsonToken.START_OBJECT );
          wrapper.nextField( "@version" );
          wrapper.nextToken( JsonToken.VALUE_STRING );
          wrapper.nextField( "aValue" );
          wrapper.nextToken( JsonToken.VALUE_NUMBER_INT );
          wrapper.nextToken( JsonToken.END_OBJECT );

          wrapper.nextToken( JsonToken.END_OBJECT );
          wrapper.ensureObjectClosed();
        } finally {
          in.close();
        }
      }

      @Override
      protected void verifyMocks() throws Exception {
        Mockito.verify( listener ).skippingField( parser, "_filter1" );
        Mockito.verify( listener ).skippingFieldValue( parser, "_filter1" );
        Mockito.verify( listener ).skippingField( parser, "_filter2" );
        Mockito.verify( listener ).skippingFieldValue( parser, "_filter2" );
        Mockito.verify( listener ).skippingField( parser, "_filter3" );
        Mockito.verify( listener ).skippingFieldValue( parser, "_filter3" );
        Mockito.verify( listener ).skippingField( parser, "_filter4" );
        Mockito.verify( listener ).skippingFieldValue( parser, "_filter4" );
        Mockito.verify( listener ).skippingField( parser, "_filter5" );
        Mockito.verify( listener ).skippingFieldValue( parser, "_filter5" );
        Mockito.verify( listener ).skippingField( parser, "_filter6" );
        Mockito.verify( listener ).skippingFieldValue( parser, "_filter6" );

        Mockito.verifyNoMoreInteractions( listener );
      }
    }.run();
  }

  @Test
  public void testIt() throws Exception {
    JsonFactory jsonFactory = new JsonFactory();
    InputStream in = getClass().getResourceAsStream( "filter.json" );
    try {
      FilteringJsonParser parser = new FilteringJsonParser( jsonFactory.createParser( in ), new Filter() {
        @Override
        public boolean shallFilterOut( @Nonnull JsonParser parser ) throws IOException, JsonParseException {
          return parser.getCurrentName().startsWith( "_" );
        }
      } );

      JacksonParserWrapper wrapper = new JacksonParserWrapper( parser );

      wrapper.nextToken( JsonToken.START_OBJECT );
      wrapper.nextField( "id" );
      wrapper.nextToken( JsonToken.VALUE_STRING );

      wrapper.nextField( "key" );
      wrapper.nextToken( JsonToken.START_OBJECT );
      wrapper.nextField( "description" );
      wrapper.nextToken( JsonToken.VALUE_STRING );
      wrapper.nextToken( JsonToken.END_OBJECT );

      wrapper.nextField( "value" );
      wrapper.nextToken( JsonToken.START_OBJECT );
      wrapper.nextField( "@version" );
      wrapper.nextToken( JsonToken.VALUE_STRING );
      wrapper.nextField( "aValue" );
      wrapper.nextToken( JsonToken.VALUE_NUMBER_INT );
      wrapper.nextToken( JsonToken.END_OBJECT );

      wrapper.nextToken( JsonToken.END_OBJECT );
      wrapper.ensureObjectClosed();
    } finally {
      in.close();
    }
  }
}
