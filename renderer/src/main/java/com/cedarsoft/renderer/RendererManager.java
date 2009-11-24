package com.cedarsoft.renderer;

import com.cedarsoft.registry.TypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Manages {@link Renderer}
 */
public class RendererManager {
  @NotNull
  private final TypeRegistry<Renderer<?, Object>> registry;

  public RendererManager() {
    this( false );
  }

  public RendererManager( boolean registerSuperTypes ) {
    registry = new TypeRegistry<Renderer<?, Object>>( registerSuperTypes );
  }

  public void setRenderer( @NotNull Map<Class<?>, Renderer<?, Object>> renderer ) {
    registry.setElements( renderer );
  }

  public <T> void addRenderer( @NotNull Class<T> type, @NotNull Renderer<? super T, Object> renderer ) {
    registry.addElement( type, renderer );
  }

  @NotNull
  public <T> Renderer<? super T, Object> getRenderer( @NotNull Class<T> type ) throws IllegalArgumentException {
    return ( Renderer<T, Object> ) registry.getElement( type );
  }
}
