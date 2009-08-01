package com.cedarsoft.gdao;

import org.hibernate.Criteria;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 *
 */
public abstract class AbstractCollectionFinder<T> extends AbstractFinder<T, Collection<? extends T>> implements CollectionFinder<T> {
  protected AbstractCollectionFinder( @NotNull Class<T> type ) {
    super( type );
  }

  @Override
  @NotNull
  protected Collection<? extends T> execute( @NotNull Criteria criteria ) {
    return criteria.list();
  }
}
