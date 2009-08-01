package com.cedarsoft.gdao;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public abstract class AbstractInstanceFinder<T> extends AbstractFinder<T, T> implements InstanceFinder<T> {
  protected AbstractInstanceFinder( @NotNull Class<T> type ) {
    super( type );
  }

  @Override
  @Nullable
  protected T execute( @NotNull Criteria criteria ) throws HibernateException {
    return type.cast( criteria.uniqueResult() );
  }
}

