package com.cedarsoft.gdao;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;

/**
 * Finder implementation that can be used with {@link GenericDao#find(Finder)}
 *
 * @param <R>: The return type (e.g. a Collection<T>)
 */
public interface Finder<R> {
  /**
   * Finds the given objects from the session
   *
   * @param session the session
   * @return the selected object
   */
  @NotNull
  R find( @NotNull Session session ) throws DataAccessException;
}