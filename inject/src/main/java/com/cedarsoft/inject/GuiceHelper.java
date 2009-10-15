package com.cedarsoft.inject;

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
  public static <T> ParameterizedType superCollectionOf( @NotNull Type type ) {
    return Types.newParameterizedType( Collection.class, Types.subtypeOf( type ) );
  }

  @NotNull
  public static <T> ParameterizedType superListOf( @NotNull Type type ) {
    return Types.newParameterizedType( List.class, Types.subtypeOf( type ) );
  }
}
