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

package com.cedarsoft.configuration.xml;

import com.cedarsoft.configuration.DefaultValueProvider;
import org.apache.commons.configuration.Configuration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * Offers common access to configurations.
 * Implementations are independant of the type.
 */
public class ConfigurationAccess<T> {
  @NotNull
  private final Class<? extends T> type;
  @NotNull
  @NonNls
  private final String key;
  @NotNull
  private final ConfigurationResolver<T> resolver;
  @NotNull
  private final DefaultValueProvider defaultValueProvider;
  @NotNull
  private final Configuration configuration;

  public ConfigurationAccess( @NotNull Configuration configuration, @NotNull Class<? extends T> type, @NotNull @NonNls String key, @NotNull final T defaultValue ) {
    this( configuration, type, key, new DefaultValueProvider() {
      @Override
      @NotNull
      public <T> T getDefaultValue( @NotNull @NonNls String key, @NotNull Class<T> type ) {
        return type.cast( defaultValue );
      }
    } );
  }

  public ConfigurationAccess( @NotNull Configuration configuration, @NotNull Class<? extends T> type, @NotNull @NonNls String key, @NotNull DefaultValueProvider defaultValueProvider ) {
    this( configuration, type, key, defaultValueProvider, ConfigurationResolver.getResolver( type ) );
  }

  public ConfigurationAccess( @NotNull Configuration configuration, @NotNull Class<? extends T> type, @NotNull @NonNls String key, @NotNull DefaultValueProvider defaultValueProvider, @NotNull ConfigurationResolver<T> resolver ) {
    this.configuration = configuration;
    this.type = type;
    this.key = key;
    this.defaultValueProvider = defaultValueProvider;
    this.resolver = resolver;
  }

  @Nullable
  public T resolve() {
    try {
      T resolved = resolver.resolve( configuration, key );
      if ( resolved != null ) {
        return resolved;
      }
    } catch ( NoSuchElementException ignore ) {
    }
    T defaultValue = defaultValueProvider.getDefaultValue( key, type );
    if ( defaultValue != null ) {
      resolver.store( configuration, key, defaultValue );
    }
    return defaultValue;
  }

  /**
   * Resolves the value
   *
   * @return the resolved value
   *
   * @throws IllegalArgumentException if the key is invalid
   */
  @NotNull
  public T resolveSafe() throws IllegalArgumentException {
    T resolved = resolve();
    if ( resolved == null ) {
      throw new IllegalArgumentException( "No configured value found for " + key );
    }
    return resolved;
  }

  @NotNull
  public Class<? extends T> getType() {
    return type;
  }

  @NotNull
  @NonNls
  public String getKey() {
    return key;
  }

  public void store( @NotNull T value ) {
    resolver.store( configuration, key, value );
  }
}
