package com.cedarsoft.gdao;

import com.cedarsoft.WriteableObjectAccess;
import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.List;

/**
 * Offers implementations of the methods of {@link WriteableObjectAccess}
 *
 * @param <T> the type
 */
public abstract class AbstractGenericDao<T> implements GenericDao<T> {
  @Override
  public final void remove( @NotNull T element ) {
    delete( element );
  }

  @Override
  public final void add( @NotNull T element ) {
    saveOrUpdate( element );
  }

  @Override
  public void commit( @NotNull T element ) {
    update( element );
  }

  @Override
  @NotNull
  public final List<? extends T> getElements() {
    return findAll();
  }

  @Override
  public void setElements( @NotNull List<? extends T> elements ) {
    throw new UnsupportedOperationException( "Not supported for daos" );
  }
}
