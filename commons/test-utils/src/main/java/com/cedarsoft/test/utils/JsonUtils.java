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

package com.cedarsoft.test.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.*;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JsonUtils {
  private JsonUtils() {
  }

  public static void assertJsonEquals(@Nonnull URL expected, @Nullable String actual) throws SAXException, IOException {
    assertJsonEquals(expected, actual, Charsets.UTF_8);
  }

  public static void assertJsonEquals(@Nonnull URL expected, @Nullable String actual, @Nonnull Charset charset) throws IOException {
    assertJsonEquals(AssertUtils.toString(expected, charset), actual);
  }

  public static void assertJsonEquals(@Nullable String expected, @Nullable String actual) throws IOException {
    if (actual == null || actual.trim().isEmpty()) {
      throw new ComparisonFailure("Empty actual json", formatJson(expected).trim(), formatJson(actual).trim());
    }
    if (expected == null || expected.trim().isEmpty()) {
      throw new ComparisonFailure("Empty expected json", formatJson(expected).trim(), formatJson(actual).trim());
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode expectedTree = mapper.readTree(expected);
      JsonNode actualTree = mapper.readTree(actual);

      if (!expectedTree.equals(actualTree)) {
        throw new ComparisonFailure("JSON comparison failed", formatJson(expected).trim(), formatJson(actual).trim());
      }
    } catch ( JsonProcessingException e ) {
      throw new ComparisonFailure("JSON parsing error (" + e.getMessage() + ")", formatJson(expected).trim(), formatJson(actual).trim());
    }
  }

  @Deprecated
  public static void assertJsonEquals(@Nullable String err, @Nullable String expected, @Nullable String actual) throws IOException, ComparisonFailure {
    assertJsonEquals(expected, actual);
  }

  @Nonnull
  public static String formatJson( @Nullable String unformated ) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode tree = mapper.readTree( unformated );

      StringWriter out = new StringWriter();
      JsonGenerator jsonGenerator = mapper.getFactory().createGenerator( out );

      jsonGenerator.useDefaultPrettyPrinter();
      jsonGenerator.writeTree( tree );

      return out.toString();
    } catch ( Exception ignore ) {
      //Do not format if it is not possible...
      return String.valueOf( unformated );
    }
  }
}
