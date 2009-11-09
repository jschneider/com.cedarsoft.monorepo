package com.cedarsoft.commons.struct;

import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.util.Collections;
import java.util.List;

/**
 * Represents a leaf. This node cannot have any children.
 */
public class LeafNode implements Node {
  @NotNull
  private final Lookup lookup;
  @NonNls
  @NotNull
  private final String name;
  @Nullable
  private Node parent;

  public LeafNode( @NotNull String name, @NotNull Lookup lookup ) {
    this.name = name;
    this.lookup = lookup;
  }

  @Override
  @NotNull
  public Lookup getLookup() {
    return lookup;
  }

  @Override
  @NotNull
  @NonNls
  public String getName() {
    return name;
  }

  @Override
  @NotNull
  public List<? extends Node> getChildren() {
    return Collections.emptyList();
  }

  @Override
  public void addStructureListener( @NotNull StructureListener structureListener ) {
  }

  @Override
  public void addStructureListenerWeak( @NotNull StructureListener structureListener ) {
  }

  @Override
  public void removeStructureListener( @NotNull StructureListener structureListener ) {
  }

  @Override
  @NotNull
  public Node findChild( @NotNull @NonNls String childName ) throws ChildNotFoundException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addChild( @NotNull Node child ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addChild( int index, @NotNull Node child ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void detachChild( @NotNull Node child ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void detachChild( int index ) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Nullable
  public Node getParent() {
    return parent;
  }

  @Override
  public void setParent( @Nullable Node parent ) {
    this.parent = parent;
  }

  @Override
  @NotNull
  public Path getPath() {
    return Path.buildPath( this );
  }

  @Override
  public boolean isChild( @NotNull StructPart child ) {
    return false;
  }

  @Override
  public boolean hasParent() {
    return parent != null;
  }
}
