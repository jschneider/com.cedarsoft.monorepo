package com.cedarsoft.hierarchy;

import com.cedarsoft.registry.TypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Manages {@link ChildDetector}s
 */
public class ChildDetectorManager {
  @NotNull
  private final TypeRegistry<ChildDetector<?, ?>> registry;

  public ChildDetectorManager() {
    this( true );
  }

  public ChildDetectorManager( boolean registerSuperTypes ) {
    registry = new TypeRegistry<ChildDetector<?, ?>>( registerSuperTypes );
  }

  public <P, C> void addChildDetector( @NotNull Class<P> parentType, @NotNull ChildDetector<P, C> childDetector ) {
    registry.addElement( parentType, childDetector );
  }

  public void setChildDetectors( Map<Class<?>, ChildDetector<?, ?>> childDetectors ) {
    registry.setElements( childDetectors );
  }

  @NotNull
  public <P, C> ChildDetector<P, C> getChildDetector( @NotNull Class<P> parentType ) throws IllegalArgumentException {
    return ( ChildDetector<P, C> ) registry.getElement( parentType );
  }
}
