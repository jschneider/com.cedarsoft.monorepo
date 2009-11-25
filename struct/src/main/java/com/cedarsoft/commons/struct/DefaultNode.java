package com.cedarsoft.commons.struct;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 */
public class DefaultNode implements Node {
  @NotNull
  @NonNls
  private final String name;
  @NotNull
  private final ChildrenSupport childrenSupport;
  @NotNull
  private final Lookup lookup;
  @Nullable
  private Node parent;

  public DefaultNode( @NonNls @NotNull String name ) {
    this( name, new DefaultChildrenSupport(), Lookups.emtyLookup() );
  }

  public DefaultNode( @NonNls @NotNull String name, @NotNull Lookup lookup ) {
    this( name, new DefaultChildrenSupport(), lookup );
  }

  public DefaultNode( @NonNls @NotNull String name, @NotNull ChildrenSupport childrenSupport, @NotNull Lookup lookup ) {
    this.lookup = lookup;
    this.childrenSupport = childrenSupport;
    this.childrenSupport.setParentNode( this );
    this.name = name;
  }

  @Override
  public void setParent( @Nullable Node parent ) {
    if ( parent != null && !parent.isChild( this ) ) {
      throw new IllegalArgumentException( "invalid parent " + parent );
    }
    this.parent = parent;
  }

  @Override
  public boolean isChild( @NotNull StructPart child ) {
    return childrenSupport.isChild( child );
  }

  @Override
  public boolean hasParent() {
    return parent != null;
  }

  @Override
  @NotNull
  public Node findChild( @NotNull @NonNls String childName ) throws ChildNotFoundException {
    return childrenSupport.findChild( childName );
  }

  @Override
  @NotNull
  @NonNls
  public String getName() {
    return name;
  }

  @Override
  @NotNull
  public Lookup getLookup() {
    return lookup;
  }

  @Override
  @NotNull
  public List<? extends Node> getChildren() {
    return childrenSupport.getChildren();
  }

  public void setChildren( @NotNull List<? extends Node> children ) {
    childrenSupport.setChildren( children );
  }

  @Override
  public void addChild( int index, @NotNull Node child ) {
    childrenSupport.addChild( index, child );
  }

  @Override
  public void addChild( @NotNull Node child ) {
    childrenSupport.addChild( child );
  }

  @Override
  public void detachChild( @NotNull Node child ) {
    childrenSupport.detachChild( child );
  }

  @Override
  public void detachChild( int index ) {
    childrenSupport.detachChild( index );
  }

  @Override
  @Nullable
  public Node getParent() {
    return parent;
  }

  @Override
  @NotNull
  public Path getPath() {
    return Path.buildPath( this );
  }

  @Override
  public void addStructureListener( @NotNull StructureListener structureListener ) {
    childrenSupport.addStructureListener( structureListener );
  }

  @Override
  public void addStructureListenerWeak( @NotNull StructureListener structureListener ) {
    childrenSupport.addStructureListenerWeak( structureListener );
  }

  @Override
  public void removeStructureListener( @NotNull StructureListener structureListener ) {
    childrenSupport.removeStructureListener( structureListener );
  }

  @NotNull
  ChildrenSupport getChildrenSupport() {
    return childrenSupport;
  }

  @Override
  public String toString() {
    return "DefaultNode{" +
      "name='" + name + '\'' +
      ", parent=" + parent +
      ", children.size=" + childrenSupport.getChildren().size() +
      '}';
  }

  /**
   * Detaches all children
   */
  public void detachChildren() {
    childrenSupport.detachChildren();
  }
}
