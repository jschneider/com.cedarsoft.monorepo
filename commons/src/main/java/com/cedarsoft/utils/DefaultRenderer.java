package com.cedarsoft.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.Override;

/**
 * Default implementation of {@link Renderer} that simply returns
 * {@link String#valueOf(Object)}.
 */
public class DefaultRenderer implements Renderer<Object, Object> {
  @Override
  @NotNull
  public String render( @NotNull Object object, Object context ) {
    return String.valueOf( object );
  }
}
