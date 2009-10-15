package com.cedarsoft.inject;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.util.Types;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class GuiceHelper {
  private GuiceHelper() {
  }

  @NotNull
  public static ParameterizedType superCollectionOf( @NotNull Type type ) {
    return Types.newParameterizedType( Collection.class, Types.subtypeOf( type ) );
  }

  @NotNull
  public static ParameterizedType superListOf( @NotNull Type type ) {
    return Types.newParameterizedType( List.class, Types.subtypeOf( type ) );
  }

  /**
   * Binds a wildcard collection to the set of that type
   *
   * @param binder the binder
   * @param type   the type
   */
  public static <T> void bindWildcardCollectionForSet( @NotNull Binder binder, @NotNull Type type ) {
    binder.bind( ( Key<Collection<? extends T>> ) Key.get( GuiceHelper.superCollectionOf( type ) ) ).to( ( Key<? extends Collection<? extends T>> ) Key.get( Types.setOf( type ) ) );
  }
}
