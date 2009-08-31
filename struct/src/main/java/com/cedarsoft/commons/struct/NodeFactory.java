package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;

/**
 *
 */
public interface NodeFactory {
  @NotNull 
  Node createNode( @NotNull @NonNls String name );
}