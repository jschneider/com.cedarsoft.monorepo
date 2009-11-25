package com.cedarsoft;

import org.jetbrains.annotations.NotNull;

/**
 * @param <T> the type that is converted
 * @param <C> the target type this converts to
 */
public interface Converter<T, C> {
  @NotNull
  C convert( @NotNull T toConvert );
}