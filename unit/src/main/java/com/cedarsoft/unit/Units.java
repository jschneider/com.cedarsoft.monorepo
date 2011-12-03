/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cedarsoft.unit;

import com.cedarsoft.unit.si.SIBaseUnit;
import com.cedarsoft.unit.si.SiDerivedUnit;

import javax.annotation.Nonnull;

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

  @Nonnull
  public static String getSymbol( @Nonnull Class<? extends Annotation> unitType ) {
    Symbol symbol = unitType.getAnnotation( Symbol.class );
    if ( symbol == null ) {
      throw new IllegalArgumentException( "Missing annotation " + Symbol.class.getName() + " for " + unitType );
    }

    return symbol.value();
  }

  @Nonnull
  public static String getName( @Nonnull Class<? extends Annotation> unitType ) {
    Name name = unitType.getAnnotation( Name.class );
    if ( name == null ) {
      throw new IllegalArgumentException( "Missing annotation " + Name.class.getName() + " for " + unitType );
    }

    return name.value();
  }

  public static boolean isSiBaseUnit( @Nonnull Class<? extends Annotation> unitType ) {
    return unitType.getAnnotation( SIBaseUnit.class ) != null;
  }

  public static boolean isDerivedSiUnit( @Nonnull Class<? extends Annotation> unitType ) {
    return unitType.getAnnotation( SiDerivedUnit.class ) != null;
  }

  @Nonnull
  public static List<? extends Class<? extends Annotation>> getDerivedFrom( @Nonnull Class<? extends Annotation> unitType ) {
    SiDerivedUnit derived = unitType.getAnnotation( SiDerivedUnit.class );
    if ( derived == null ) {
      throw new IllegalArgumentException( "Not derived. " + unitType );
    }

    return new ArrayList<Class<? extends Annotation>>( Arrays.asList( derived.value() ) );
  }

  @Nonnull
  public static List<? extends String> getDefinitions( @Nonnull Class<? extends Annotation> unitType ) {
    Definition definition = unitType.getAnnotation( Definition.class );
    if ( definition == null ) {
      throw new IllegalArgumentException( "No definition found for " + unitType );
    }

    return new ArrayList<String>( Arrays.asList( definition.value() ) );
  }
}