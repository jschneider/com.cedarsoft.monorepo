package com.cedarsoft.commons.struct;

import com.cedarsoft.CanceledException;
import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * A node factory
 */
public interface NodeFactory {
  /**
   * Creates a node for the given name
   *
   * @param name    the name
   * @param context the context
   * @return the node
   */
  @NotNull
  Node createNode( @NotNull @NonNls String name, @NotNull Lookup context ) throws CanceledException;
}