package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link ChildrenSupport}
 */
public class DefaultChildrenSupport implements ChildrenSupport {
  private Node parentNode;

  private List<Node> nodes = new ArrayList<Node>();
  private List<Node> nodesUm = Collections.unmodifiableList( nodes );

  @Override
  @NotNull
  public List<? extends Node> getChildren() {
    //noinspection ReturnOfCollectionOrArrayField
    return nodesUm;
  }

  @Override
  public void addChild( int index, @NotNull Node child ) {
    validateName( child.getName() );

    nodes.add( index, child );
    child.setParent( getParentNode() );
    notifyChildAdded( child, index );
  }

  @Override
  public void addChild( @NotNull Node child ) {
    addChild( nodes.size(), child );
  }

  @Override
  public void setChildren( @NotNull List<? extends Node> children ) {
    detachChildren();
    for ( Node child : children ) {
      addChild( child );
    }
  }

  /**
   * Detaches all children
   */
  @Override
  public void detachChildren() {
    if ( !nodes.isEmpty() ) {
      for ( Node node : new ArrayList<Node>( nodes ) ) {
        detachChild( node );
      }
    }
  }

  private void validateName( @NotNull String name ) {
    for ( Node node : nodes ) {
      if ( node.getName().equals( name ) ) {
        throw new IllegalArgumentException( "There still exists a node with the name " + name );
      }
    }
  }

  @Override
  public void detachChild( @NotNull Node child ) {
    int oldIndex = nodes.indexOf( child );
    if ( oldIndex < 0 ) {
      throw new IllegalArgumentException( "Invalid child. " + child );
    }
    detachChild( oldIndex, child );
  }

  @Override
  public void detachChild( int index ) {
    detachChild( index, nodes.get( index ) );
  }

  private void detachChild( int index, @NotNull Node childToDetach ) {
    nodes.remove( index );
    childToDetach.setParent( null );
    notifyChildDetached( childToDetach, index );
  }

  @Override
  @NotNull
  public Node getParentNode() {
    return parentNode;
  }

  @Override
  public void setParentNode( @NotNull Node parentNode ) {
    this.parentNode = parentNode;
  }

  @Override
  public boolean isChild( @NotNull StructPart child ) {
    //noinspection SuspiciousMethodCalls
    return nodes.contains( child );
  }

  private final List<StructureListener> structureListeners = new ArrayList<StructureListener>();

  @Override
  public void addStructureListener( @NotNull StructureListener structureListener ) {
    structureListeners.add( structureListener );
  }

  @Override
  public void addStructureListenerWeak( @NotNull StructureListener structureListener ) {
    structureListeners.add( new WeakStructureListener( structureListener ) );
  }

  @Override
  public void removeStructureListener( @NotNull StructureListener structureListener ) {
    for ( Iterator<StructureListener> it = structureListeners.iterator(); it.hasNext(); ) {
      StructureListener listener = it.next();
      if ( listener == structureListener || listener instanceof WeakStructureListener && ( ( WeakStructureListener ) listener ).getWrappedListener() == structureListener ) {
        it.remove();
      }
    }
    structureListeners.remove( structureListener );
  }

  @Override
  @NotNull
  public List<? extends StructureListener> getStructureListeners() {
    return Collections.unmodifiableList( structureListeners );
  }

  @Override
  @NotNull
  public Node findChild( @NotNull String childName ) throws ChildNotFoundException {
    for ( Node node : nodes ) {
      if ( node.getName().equals( childName ) ) {
        return node;
      }
    }
    throw new ChildNotFoundException( new Path( childName ) );
  }

  private void notifyChildAdded( @NotNull Node child, int index ) {
    StructureChangedEvent event = new StructureChangedEvent( getParentNode(), StructureChangedEvent.Type.Add, child, index );
    for ( StructureListener listener : new ArrayList<StructureListener>( structureListeners ) ) {
      listener.childAdded( event );
    }
  }

  private void notifyChildDetached( @NotNull Node child, int index ) {
    StructureChangedEvent event = new StructureChangedEvent( getParentNode(), StructureChangedEvent.Type.Remove, child, index );
    for ( StructureListener listener : new ArrayList<StructureListener>( structureListeners ) ) {
      listener.childDetached( event );
    }
  }
}
