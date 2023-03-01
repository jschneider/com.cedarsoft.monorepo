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
package it.neckar.open.serialization.jackson.filter;

import it.neckar.open.serialization.SerializationException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserDelegate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is a special class that filters out specific fields
 *
 */
public class FilteringJsonParser extends JsonParserDelegate {
  @Nonnull
  private final Filter filter;
  @Nonnull
  private final List<FilteredParserListener> filteredParserListeners = new CopyOnWriteArrayList<FilteredParserListener>();

  public FilteringJsonParser( @Nonnull JsonParser parser, @Nonnull Filter filter ) {
    super( parser );
    this.filter = filter;
  }

  public void addListener( @Nonnull FilteredParserListener listener ) {
    this.filteredParserListeners.add( listener );
  }

  public void removeListener( @Nonnull FilteredParserListener listener ) {
    this.filteredParserListeners.add( listener );
  }

  @Override
  public JsonToken nextToken() throws IOException, JsonParseException {
    super.nextToken();

    //If it is a filtered field, please skip it
    while ( getCurrentToken() == JsonToken.FIELD_NAME && filter.shallFilterOut( this ) ) {
      skipToNextField();
    }

    return getCurrentToken();
  }

  protected void skipToNextField() throws IOException {
    String fieldName = getCurrentName();

    notifySkippingField( fieldName );
    JsonToken token = super.nextToken();

    if ( token == JsonToken.START_OBJECT || token == JsonToken.START_ARRAY ) {
      notifySkippingValue(fieldName);
      skipChildren();
      super.nextToken();
    } else if ( isValue( token ) ) {
      notifySkippingValue(fieldName);
      super.nextToken();
    } else {
      throw new SerializationException( delegate.getCurrentLocation(), SerializationException.Details.INVALID_STATE, delegate.getClass().getName() );
    }
  }

  private void notifySkippingValue( @Nonnull String fieldName ) throws IOException {
    for ( FilteredParserListener filteredParserListener : filteredParserListeners ) {
      filteredParserListener.skippingFieldValue( this, fieldName );
    }
  }

  private void notifySkippingField( @Nonnull String fieldName ) throws IOException {
    for ( FilteredParserListener filteredParserListener : filteredParserListeners ) {
      filteredParserListener.skippingField( this, fieldName );
    }
  }

  private static boolean isValue( @Nonnull JsonToken token ) {
    if ( token == JsonToken.VALUE_EMBEDDED_OBJECT ) {
      return true;
    }
    if ( token == JsonToken.VALUE_FALSE ) {
      return true;
    }
    if ( token == JsonToken.VALUE_NULL ) {
      return true;
    }
    if ( token == JsonToken.VALUE_NUMBER_FLOAT ) {
      return true;
    }
    if ( token == JsonToken.VALUE_NUMBER_INT ) {
      return true;
    }
    if ( token == JsonToken.VALUE_STRING ) {
      return true;
    }
    if ( token == JsonToken.VALUE_STRING ) {
      return true;
    }

    return false;
  }
}
