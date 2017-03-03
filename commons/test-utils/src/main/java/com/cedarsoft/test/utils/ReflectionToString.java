package com.cedarsoft.test.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ReflectionToString {
  private ReflectionToString() {
  }

  @Nonnull
  public static String toString( @Nullable Object object ) {
    if ( object == null ) {
      return "<null>";
    }

    StringBuilder builder = new StringBuilder();
    builder.append( object.getClass().getName() ).append( " {\n" );

    Class<?> daClass = object.getClass();
    while ( daClass != null ) {
      for ( Field field : daClass.getDeclaredFields() ) {
        field.setAccessible( true );

        builder.append( "\t" );
        builder.append( field.getName() ).append( ": " );
        try {
          builder.append( field.get( object ) );
        } catch ( IllegalAccessException e ) {
          builder.append( "<failed due to " ).append( e.getMessage() ).append( ">" );
        }
        builder.append( "\n" );
      }
      daClass = daClass.getSuperclass();
    }

    builder.append( "}" );
    return builder.toString();
  }

}
