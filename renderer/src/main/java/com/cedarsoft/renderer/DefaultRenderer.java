package com.cedarsoft.renderer;

import org.jetbrains.annotations.NotNull;

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
