package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NotNull;

/**
 * Helper method for nodes
 */
public class Nodes {
  private Nodes() {
  }

  /**
   * Returns whether the given node is a linked node
   *
   * @param node the node
   * @return true if the given node is a linked node, false otherwise
   */
  public static boolean isLinkedNode( @NotNull Node node ) {
    return node instanceof LinkedNode;
  }
}
