package com.cedarsoft.commons.struct;

import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A struct part is a read only view. For adding/removing children a {@link Node} is needed.
 */
public interface StructPart {
  /**
   * Returns the name of the node
   *
   * @return the name
   */
  @NotNull
  @NonNls
  String getName();

  /**
   * Returns the lookup associated with the node
   *
   * @return the lookup
   */
  @NotNull
  Lookup getLookup();

  /**
   * Returns a list containing all children
   *
   * @return a list containing all children
   */
  @NotNull
  List<? extends StructPart> getChildren();

  /**
   * Returns the parent (if it has one)
   *
   * @return the parent
   */
  @Nullable
  StructPart getParent();

  /**
   * Adds a child listener. Be careful - most of the time the listener should
   * only be added weak using {@link #addStructureListenerWeak(StructureListener)}
   *
   * @param structureListener the chid listener
   */
  void addStructureListener( @NotNull StructureListener structureListener );

  /**
   * Adds a child listener wrapped within a weak listener
   *
   * @param structureListener the chid listener
   */
  void addStructureListenerWeak( @NotNull StructureListener structureListener );

  /**
   * Removes the child listener
   *
   * @param structureListener the child listener
   */
  void removeStructureListener( @NotNull StructureListener structureListener );

  /**
   * Returns the path of this node
   *
   * @return the path
   */
  @NotNull
  Path getPath();

  /**
   * Returns whether the given node is a child of this or not
   *
   * @param child the possible child
   * @return true if the given node is a child, false otherwise
   */
  boolean isChild( @NotNull StructPart child );

  /**
   * Returns true if the node has a parent
   *
   * @return true if the node has a parent, false otherwise
   */
  boolean hasParent();

  /**
   * Returns the child with the given childName
   *
   * @param childName the childName of the children
   * @return the child with the given childName
   */
  @NotNull
  StructPart findChild( @NotNull @NonNls String childName ) throws ChildNotFoundException;
}