package com.cedarsoft.gdao;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Selects a collection.
 *
 * @param <T> the type of the collection this finder returns.
 */
public interface CollectionFinder<T> extends Finder<Collection<? extends T>> {
  @NotNull
  @Override
  Collection<? extends T> find( @NotNull Session session );
}
