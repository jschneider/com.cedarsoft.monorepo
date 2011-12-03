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

package com.cedarsoft.unit.prefix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Prefixed {
  private Prefixed() {
  }


  public static double getFactor( @Nonnull Class<? extends Annotation> unit ) {
    Prefix prefixAnnotation = getPrefixAnnotation( unit );
    if ( prefixAnnotation == null ) {
      throw new IllegalArgumentException( unit.getName() + " is not annotated with " + Prefix.class.getName() );
    }

    return prefixAnnotation.value();
  }

  public static boolean isPrefixed( @Nonnull Class<? extends Annotation> unit ) {
    Prefix prefixAnnotation = getPrefixAnnotation( unit );
    return prefixAnnotation != null;
  }

  @Nullable
  public static Prefix getPrefixAnnotation( @Nonnull AnnotatedElement unit ) {
    {
      Prefix prefix = unit.getAnnotation( Prefix.class );
      if ( prefix != null ) {
        return prefix;
      }
    }

    for ( Annotation annotation : unit.getAnnotations() ) {
      Prefix prefix = annotation.annotationType().getAnnotation( Prefix.class );
      if ( prefix != null ) {
        return prefix;
      }
    }

    return null;
  }
}
