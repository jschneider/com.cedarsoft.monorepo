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

package com.cedarsoft;

import org.apache.commons.lang.ObjectUtils;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * a reflection based matcher
 *
 * @param <T> the type
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ReflectionMatcher<T> implements IArgumentMatcher {
  /**
   * <p>create</p>
   *
   * @param object a T object.
   * @param blacklistedFieldNames a {@link java.lang.String} object.
   * @return a T object.
   */
  @Nullable
  public static <T> T create( @Nullable T object, @NotNull @NonNls String... blacklistedFieldNames ) {
    EasyMock.reportMatcher( new ReflectionMatcher<T>( object, blacklistedFieldNames ) );
    return object;
  }

  @Nullable
  private final T object;

  @NotNull
  private final Set<String> blacklistedFieldNames = new HashSet<String>();

  /**
   * <p>Constructor for ReflectionMatcher.</p>
   *
   * @param object a T object.
   * @param blacklistedFieldNames a {@link java.lang.String} object.
   */
  public ReflectionMatcher( @Nullable T object, @NotNull @NonNls String... blacklistedFieldNames ) {
    this.object = object;
    this.blacklistedFieldNames.addAll( Arrays.asList( blacklistedFieldNames ) );
  }

  /** {@inheritDoc} */
  @Override
  public boolean matches( Object o ) {
    if ( object == null && o == null ) {
      return true;
    }

    if ( object == null || o == null ) {
      return false;
    }

    Class<?> type = object.getClass();
    if ( !type.equals( o.getClass() ) ) {
      return false;
    }

    while ( type != null ) {
      if ( !compareFields( type, object, ( T ) o ) ) {
        return false;
      }
      type = type.getSuperclass();
    }

    return true;
  }

  private boolean compareFields( @NotNull Class<?> type, @NotNull T object, @NotNull T other ) {
    try {
      for ( Field field : type.getDeclaredFields() ) {
        if ( blacklistedFieldNames.contains( field.getName() ) ) {
          continue;
        }

        if ( field.isSynthetic() ) {
          continue;
        }

        field.setAccessible( true );

        Object myValue = field.get( object );
        Object otherValue = field.get( other );

        if ( !ObjectUtils.equals( myValue, otherValue ) ) {
          return false;
        }
      }
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public void appendTo( StringBuffer buffer ) {
    buffer.append( "Object did not fit: Expected <" + object + ">" );
  }
}
