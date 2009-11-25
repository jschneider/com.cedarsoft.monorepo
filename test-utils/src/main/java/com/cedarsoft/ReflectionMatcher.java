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
 */
public class ReflectionMatcher<T> implements IArgumentMatcher {
  @Nullable
  public static <T> T create( @Nullable T object, @NotNull @NonNls String... blacklistedFieldNames ) {
    EasyMock.reportMatcher( new ReflectionMatcher<T>( object, blacklistedFieldNames ) );
    return object;
  }

  @Nullable
  private final T object;

  @NotNull
  private final Set<String> blacklistedFieldNames = new HashSet<String>();

  public ReflectionMatcher( @Nullable T object, @NotNull @NonNls String... blacklistedFieldNames ) {
    this.object = object;
    this.blacklistedFieldNames.addAll( Arrays.asList( blacklistedFieldNames ) );
  }

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

  @Override
  public void appendTo( StringBuffer buffer ) {
    buffer.append( "Object did not fit: Expected <" + object + ">" );
  }
}
