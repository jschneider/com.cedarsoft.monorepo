/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.convert;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This manager manages several {@link StringConverter}s and offers several convinience methods
 * to serialize/deserialization
 */
public class StringConverterManager {
  private final Map<Class<?>, StringConverter<?>> converterMap = new HashMap<Class<?>, StringConverter<?>>();

  /**
   * Creates a new StringConverterManager
   */
  public StringConverterManager() {
    this( false );
  }

  /**
   * Creates a new StringConverterManager and (optionally) adds the default converters
   *
   * @param addDefaultConverters whether the default converters shall be added
   */
  public StringConverterManager( boolean addDefaultConverters ) {
    if ( addDefaultConverters ) {
      converterMap.put( String.class, new StringStringConverter() );
      converterMap.put( Rectangle.class, new RectangleConverter() );
      converterMap.put( Point.class, new PointConverter() );
      converterMap.put( Class.class, new ClassConverter() );
      converterMap.put( File.class, new FileConverter() );
      converterMap.put( Boolean.class, new BooleanConverter() );
    }
  }

  /**
   * Returns a list with all registered converters
   *
   * @return the registered converters
   */
  public Map<Class<?>, StringConverter<?>> getConverterMap() {
    return Collections.unmodifiableMap( converterMap );
  }

  /**
   * Sets the string converters
   *
   * @param converters the string converters
   */
  public void setStringConverters( Map<Class<?>, StringConverter<?>> converters ) {
    converterMap.clear();
    converterMap.putAll( converters );
  }

  /**
   * Adds a single string converter
   *
   * @param type      the type the converter can convert
   * @param converter the converter
   */
  public <T> void addStringConverter( Class<T> type, StringConverter<T> converter ) {
    converterMap.put( type, converter );
  }

  /**
   * Find a converter for the given type
   *
   * @param type the type
   * @return the converter for this type
   */
  @NotNull
  public <T> StringConverter<T> findConverter( @NotNull Class<? extends T> type ) {
    StringConverter<?> converter = converterMap.get( type );
    if ( converter != null ) {
      //noinspection unchecked
      return ( StringConverter<T> ) converter;
    }
    throw new IllegalArgumentException( "no converter found for " + type );
  }


  /**
   * Serialize the given object
   *
   * @param object the object that is serialized
   * @return the serialized object
   */
  @NotNull
  public String serialize( @NotNull Object object ) {
    return findConverter( object.getClass() ).createRepresentation( object );
  }

  /**
   * Deserializes a string
   *
   * @param type           the type
   * @param representation the representation
   * @return the deserialized object
   */
  @NotNull
  public <T> T deserialize( @NotNull Class<T> type, @NotNull String representation ) {
    return findConverter( type ).createObject( representation );
  }

  public static class BooleanConverter implements StringConverter<Boolean> {
    @Override
    @NotNull
    @NonNls
    public String createRepresentation( @NotNull Boolean object ) {
      return object.toString();
    }

    @Override
    @NotNull
    public Boolean createObject( @NotNull @NonNls String representation ) {
      return Boolean.parseBoolean( representation );
    }
  }

  public static class FileConverter implements StringConverter<File> {
    @Override
    @NotNull
    public String createRepresentation( @NotNull File object ) {
      return object.getAbsolutePath();
    }

    @Override
    @NotNull
    public File createObject( @NotNull String representation ) {
      return new File( representation );
    }
  }

  public static class ClassConverter implements StringConverter<Class<?>> {
    @Override
    @NotNull
    public String createRepresentation( @NotNull Class<?> object ) {
      return object.getName();
    }

    @Override
    @NotNull
    public Class<?> createObject( @NotNull String representation ) {
      try {
        return Class.forName( representation );
      } catch ( ClassNotFoundException e ) {
        throw new RuntimeException( e );
      }
    }
  }

  public static class RectangleConverter implements StringConverter<Rectangle> {
    @Override
    @NotNull
    @NonNls
    public String createRepresentation( @NotNull Rectangle object ) {
      return object.x + ":" + object.y + ':' + object.width + ':' + object.height;
    }

    @Override
    @NotNull
    public Rectangle createObject( @NotNull String representation ) {
      String[] parts = representation.split( "\\:" );
      return new Rectangle( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ), Integer.parseInt( parts[2] ), Integer.parseInt( parts[3] ) );
    }
  }

  public static class PointConverter implements StringConverter<Point> {
    @Override
    @NotNull
    @NonNls
    public String createRepresentation( @NotNull Point object ) {
      return object.x + ":" + object.y;
    }

    @Override
    @NotNull
    public Point createObject( @NotNull String representation ) {
      String[] parts = representation.split( "\\:" );
      return new Point( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ) );
    }
  }

  public static class StringStringConverter implements StringConverter<String> {
    @Override
    @NotNull
    @NonNls
    public String createRepresentation( @NonNls @NotNull String object ) {
      return object;
    }

    @Override
    @NotNull
    public String createObject( @NotNull @NonNls String representation ) {
      return representation;
    }
  }
}
