package com.cedarsoft.registry;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a special registry that registers an implementation of T for
 * given type. (A map with a class as key and value T).
 * @param <T> the type that is stored within this registry
 */
public class TypeRegistry<T> {
  private final boolean searchSuperTypes;
  @NotNull
  private final Map<Class<?>, T> elements = new HashMap<Class<?>, T>();

  /**
   * Creates a new registry with
   * {@link #searchSuperTypes} set to true.
   */
  public TypeRegistry() {
    this( true );
  }

  /**
   * Creates a new registry
   *
   * @param searchSuperTypes whether the super types are used as keys
   */
  public TypeRegistry( boolean searchSuperTypes ) {
    this.searchSuperTypes = searchSuperTypes;
  }

  /**
   * Sets the elements. This method iterates over the given elements and calls {@link #addElement(Class,Object)}
   * for each entry.
   *
   * @param elements the elements that are added
   */
  public void setElements( @NotNull Map<Class<?>, T> elements ) {
    this.elements.clear();
    for ( Map.Entry<Class<?>, T> entry : elements.entrySet() ) {
      addElement( entry.getKey(), entry.getValue() );
    }
  }

  /**
   * Returns whether the search for super types is enabled
   *
   * @return whether the search for super types is enabled
   */
  public boolean isSearchSuperTypes() {
    return searchSuperTypes;
  }

  /**
   * Adds the element.
   * if {@link #isSearchSuperTypes()} is set to true, the element is also
   * added for all superclasses and declared interfaces.
   *
   * @param type the type
   * @param t    the element that is stored for the given type
   */
  public void addElement( @NotNull Class<?> type, @NotNull T t ) {
    elements.put( type, t );
  }

  /**
   * Returns the stored element for the given type
   *
   * @param type the type
   * @return the stored element
   */
  @NotNull
  public T getElement( @NotNull Class<?> type ) throws IllegalArgumentException {
    {
      T element = elements.get( type );
      if ( element != null ) {
        return element;
      }
    }

    if ( searchSuperTypes ) {
      //Now search for all interfaces
      for ( Class<?> interfaceType : type.getInterfaces() ) {
        T element = elements.get( interfaceType );
        if ( element != null ) {
          return element;
        }
      }

      //Now search for super types
      Class<?> superType = type.getSuperclass();
      if ( superType != null ) {
        try {
          return getElement( superType );
        } catch ( IllegalArgumentException ignore ) {
        }
      }
    }
    throw new IllegalArgumentException( "No element found for " + type );
  }
}
