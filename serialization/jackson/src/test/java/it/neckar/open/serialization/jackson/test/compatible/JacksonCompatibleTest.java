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
package it.neckar.open.serialization.jackson.test.compatible;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.*;

import it.neckar.open.serialization.jackson.JacksonParserWrapper;
import it.neckar.open.serialization.jackson.JacksonSupport;

import it.neckar.open.test.utils.JsonUtils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 */
public class JacksonCompatibleTest {
  private JsonFactory jsonFactory;
  private ByteArrayOutputStream out;
  private JsonGenerator generator;

  @BeforeEach
  public void setUp() throws Exception {
    jsonFactory = JacksonSupport.getJsonFactory();
    out = new ByteArrayOutputStream();
    generator = jsonFactory.createGenerator( out, JsonEncoding.UTF8 );
  }

  @Test
  public void testIt() throws Exception {
    generator.writeStartObject();

    generator.writeStringField( "street", "Schlossalle" );
    generator.writeNumberField( "number", 7 );

    generator.writeEndObject();

    generator.flush();
    generator.close();

    JsonUtils.assertJsonEquals(getClass().getResource("simple.json"), out.toString());
  }

  @Test
  public void testReadCompatible() throws Exception {
    JsonParser parser = jsonFactory.createParser( getClass().getResource( "simple.json" ) );

    JacksonParserWrapper wrapper = new JacksonParserWrapper( parser );
    wrapper.startObject();
    wrapper.nextField( "street" );

    assertThat( parser.getText() ).isEqualTo( "street" );
    assertThat( parser.nextToken() ).isEqualTo( JsonToken.VALUE_STRING );
    assertThat( parser.getText() ).isEqualTo( "Schlossalle" );

    wrapper.nextField( "number" );

    assertThat( parser.nextToken() ).isEqualTo( JsonToken.VALUE_NUMBER_INT );
    assertThat( parser.getValueAsInt() ).isEqualTo( 7 );

    assertThat( parser.nextToken() ).isEqualTo( JsonToken.END_OBJECT );

    wrapper.ensureObjectClosed();
  }
}
