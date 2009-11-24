package com.cedarsoft.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Stores the parent of children (and their indicies) in weak maps.
 */
public class ParentCache {
  /**
   * The child is the key, the parent the value
   */
  @NotNull
  private final Map<Object, WeakReference<Object>> parents = new WeakHashMap<Object, WeakReference<Object>>();
  /**
   * Contains the inidices of the child
   */
  @NotNull
  private final Map<Object, Integer> indicies = new WeakHashMap<Object, Integer>();


  /**
   * Returns the parent of the given child
   *
   * @param child the child
   * @return the parent
   */
  @Nullable
  public <P, C> P findParent( @NotNull C child ) {
    WeakReference<Object> reference = parents.get( child );
    if ( reference == null ) {
      return null;
    } else {
      return ( P ) reference.get();
    }
  }

  private void removeParent( @NotNull Object child ) {
    parents.remove( child );
  }

  private void removeIndex( @NotNull Object child ) {
    indicies.remove( child );
  }

  public void remove( @NotNull Object child ) {
    removeParent( child );
    removeIndex( child );
  }

  public void removeAll( @NotNull List<? extends Object> children ) {
    for ( Object child : children ) {
      remove( child );
    }
  }

  private <P, C> void storeParent( @NotNull C child, @NotNull P parent ) {
    Object oldParent = findParent( child );
    if ( oldParent != null ) {
      if ( parent == oldParent ) {
        return;
      } else {
        throw new IllegalStateException( "Child <" + child + "> still has a parent: <" + oldParent + ">. New parent is: <" + parent + '>' );
      }
    }
    parents.put( child, new WeakReference<Object>( parent ) );
  }

  /**
   * Stores a parent
   *
   * @param child  the child
   * @param parent the parent
   */
  public <P, C> void store( @NotNull C child, @NotNull P parent, int index ) {
    storeParent( child, parent );
    storeIndex( child, index );
  }


  public <C> void storeIndex( @NotNull C child, int index ) {
    indicies.put( child, index );
  }

  @Nullable
  public <C> Integer findIndex( @NotNull C child ) {
    return indicies.get( child );
  }

  /**
   * Returns a list containing all predecessors for the given child.
   * The first entry is the root - the last the parent of the child
   *
   * @param child the child
   * @return all predecessors for the given child
   */
  @NotNull
  public List<? extends Object> getPredecessors( @NotNull Object child ) {
    List<Object> list = new ArrayList<Object>();

    Object parent = findParent( child );
    while ( parent != null ) {
      list.add( parent );
      parent = findParent( parent );
    }

    Collections.reverse( list );
    return list;
  }

  /**
   * Returns the path (child an all its predecessors).
   * The first entry is the root - the last the child itself.
   *
   * @param child the child
   * @return a list containing the child and all its predecessors
   */
  @NotNull
  public List<? extends Object> getPath( @NotNull Object child ) {
    List<Object> path = new ArrayList<Object>( getPredecessors( child ) );
    path.add( child );
    return path;
  }

  /**
   * Stores several children
   *
   * @param parent   the parent
   * @param children the children
   */
  public void store( @NotNull Object parent, @NotNull List<? extends Object> children ) {
    for ( int index = 0; index < children.size(); index++ ) {
      Object child = children.get( index );
      store( child, parent, index );
    }
  }
}
