package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.util.List;

/**
 * A node extends the interface {@link StructPart} with read/write support.
 */
public interface Node extends StructPart {
  @Override
  @NotNull
  List<? extends Node> getChildren();

  @Override
  @NotNull
  Node findChild( @NotNull @NonNls String childName ) throws ChildNotFoundException;

  /**
   * Returns the parent (if it has one)
   *
   * @return the parent
   */
  @Override
  @Nullable
  Node getParent();

  /**
   * Adds a child
   *
   * @param child the child
   */
  void addChild( @NotNull Node child );

  /**
   * Adds a child at the given position
   *
   * @param index the index
   * @param child the child
   */
  void addChild( int index, @NotNull Node child );

  /**
   * Detaches a child
   *
   * @param child the child
   */
  void detachChild( @NotNull Node child );

  /**
   * Detaches a child
   *
   * @param index the index of the child that is detached
   */
  void detachChild( int index );

  /**
   * Sets the parent
   *
   * @param parent the parent
   */
  void setParent( @Nullable Node parent );
}
