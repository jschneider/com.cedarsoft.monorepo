package com.cedarsoft.gdao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 */
public abstract class AbstractFinder<T, R> implements Finder<R> {
  @NotNull
  protected final Class<T> type;

  protected AbstractFinder( @NotNull Class<T> type ) {
    this.type = type;
  }

  @NotNull
  public Class<T> getType() {
    return type;
  }

  /**
   * Opens a criteria for the type of this finder
   *
   * @param session the session
   * @return the
   */
  @NotNull
  public final Criteria createCriteria( @NotNull Session session ) {
    return session.createCriteria( type );
  }

  @NotNull
  public final R find( @NotNull Session session ) throws DataAccessException {
    Criteria criteria = createCriteria( session );
    addRestrictions( criteria );
    R found = execute( criteria );
    if ( found == null ) {
      throw new EmptyResultDataAccessException( 1 );
    }
    return found;
  }

  @Nullable
  protected abstract R execute( @NotNull Criteria criteria );

  protected abstract void addRestrictions( @NotNull Criteria criteria );
}
