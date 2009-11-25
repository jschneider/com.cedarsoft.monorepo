package com.cedarsoft.commons.struct;

import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This node is linked to other nodes.
 */
public class LinkedNode implements Node {
  @NotNull
  private final Node source;
  private Node parent;

  public LinkedNode( @NotNull Node source ) {
    this.source = source;
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
  public boolean hasParent() {
    return parent != null;
  }

  @Override
  @NotNull
  public Lookup getLookup() {
    return source.getLookup();
  }

  @Override
  @NotNull
  @NonNls
  public String getName() {
    return source.getName();
  }

  @Override
  @NotNull
  public List<? extends Node> getChildren() {
    return source.getChildren();
  }

  @Override
  public void addChild( int index, @NotNull Node child ) {
    source.addChild( index, child );
  }

  @Override
  public void addChild( @NotNull Node child ) {
    source.addChild( child );
  }

  @Override
  public void detachChild( @NotNull Node child ) {
    source.detachChild( child );
  }

  @Override
  public void detachChild( int index ) {
    source.detachChild( index );
  }

  @Override
  @NotNull
  public Node findChild( @NotNull @NonNls String childName ) throws ChildNotFoundException {
    return source.findChild( childName );
  }

  @Override
  @NotNull
  public Path getPath() {
    return Path.buildPath( this );
  }

  @Override
  public boolean isChild( @NotNull StructPart child ) {
    return source.isChild( child );
  }

  @Override
  public void addStructureListener( @NotNull StructureListener structureListener ) {
    source.addStructureListener( structureListener );
  }

  @Override
  public void addStructureListenerWeak( @NotNull StructureListener structureListener ) {
    source.addStructureListenerWeak( structureListener );
  }

  @Override
  public void removeStructureListener( @NotNull StructureListener structureListener ) {
    source.removeStructureListener( structureListener );
  }
}
