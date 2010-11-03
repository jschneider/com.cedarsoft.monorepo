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

package com.cedarsoft.unit;

import com.cedarsoft.unit.si.SIBaseUnit;
import com.cedarsoft.unit.si.SiDerivedUnit;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for Units
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Units {
  private Units() {
  }

  @NotNull
  @NonNls
  public static String getSymbol( @NotNull Class<? extends Annotation> unitType ) {
    Symbol symbol = unitType.getAnnotation( Symbol.class );
    if ( symbol == null ) {
      throw new IllegalArgumentException( "Missing annotation " + Symbol.class.getName() + " for " + unitType );
    }

    return symbol.value();
  }

  @NotNull
  @NonNls
  public static String getName( @NotNull Class<? extends Annotation> unitType ) {
    Name name = unitType.getAnnotation( Name.class );
    if ( name == null ) {
      throw new IllegalArgumentException( "Missing annotation " + Name.class.getName() + " for " + unitType );
    }

    return name.value();
  }

  public static boolean isSiBaseUnit( @NotNull Class<? extends Annotation> unitType ) {
    return unitType.getAnnotation( SIBaseUnit.class ) != null;
  }

  public static boolean isDerivedSiUnit( @NotNull Class<? extends Annotation> unitType ) {
    return unitType.getAnnotation( SiDerivedUnit.class ) != null;
  }

  @NotNull
  public static List<? extends Class<? extends Annotation>> getDerivedFrom( @NotNull Class<? extends Annotation> unitType ) {
    SiDerivedUnit derived = unitType.getAnnotation( SiDerivedUnit.class );
    if ( derived == null ) {
      throw new IllegalArgumentException( "Not derived. " + unitType );
    }

    return new ArrayList<Class<? extends Annotation>>( Arrays.asList( derived.value() ) );
  }

  @NotNull
  public static List<? extends String> getDefinitions( @NotNull Class<? extends Annotation> unitType ) {
    Definition definition = unitType.getAnnotation( Definition.class );
    if ( definition == null ) {
      throw new IllegalArgumentException( "No definition found for " + unitType );
    }

    return new ArrayList<String>( Arrays.asList( definition.value() ) );
  }
}