package com.cedarsoft.gdao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Offers implementations of the methods of {@link com.cedarsoft.WriteableObjectAccess}
 *
 * @param <T> the type
 */
public abstract class AbstractGenericDao<T> implements GenericDao<T> {
  public final void remove( @NotNull T element ) {
    delete( element );
  }

  public final void add( @NotNull T element ) {
    saveOrUpdate( element );
  }

  public void commit( @NotNull T element ) {
    update( element );
  }

  @NotNull
  public final List<? extends T> getElements() {
    return findAll();
  }

  public void setElements( @NotNull List<? extends T> elements ) {
    throw new UnsupportedOperationException( "Not supported for daos" );
  }
}
