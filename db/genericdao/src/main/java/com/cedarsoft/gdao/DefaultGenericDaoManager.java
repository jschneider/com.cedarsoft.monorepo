package com.cedarsoft.gdao;

import com.cedarsoft.cache.Cache;
import com.cedarsoft.cache.HashedCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.util.Iterator;

/**
 * Manages the DAOs
 */
public class DefaultGenericDaoManager implements GenericDaoManager {
  @SuppressWarnings( {"MismatchedQueryAndUpdateOfCollection"} )
  @NotNull
  private final Cache<DaoTypeDescriptor<Object>, GenericDao<Object>> daoCache;

  /**
   * Creates a new dao manager
   *
   * @param daoFactory the factory
   */
  public DefaultGenericDaoManager( @NotNull Cache.Factory<DaoTypeDescriptor<Object>, GenericDao<Object>> daoFactory ) {
    daoCache = new HashedCache<DaoTypeDescriptor<Object>, GenericDao<Object>>( daoFactory );
  }

  /**
   * Returns the dao
   *
   * @param type the type
   * @return the dao for the given type
   */
  @Override
  @NotNull
  public <T> GenericDao<T> getDao( @NotNull Class<T> type ) {
    return getDao( type, null );
  }

  @Override
  @NotNull
  public <T> GenericDao<T> getDao( @NotNull Class<T> type, @Nullable LockProvider<T> lockProvider ) {
    return ( GenericDao<T> ) daoCache.get( new DaoTypeDescriptor<T>( type, lockProvider ) );
  }

  @Override
  public void shutdown() {
    for ( Iterator<GenericDao<Object>> it = daoCache.values().iterator(); it.hasNext(); ) {
      GenericDao<Object> dao = it.next();
      dao.shutdown();
      it.remove();
    }
  }
}
