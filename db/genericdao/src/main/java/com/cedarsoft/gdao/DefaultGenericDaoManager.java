package com.cedarsoft.gdao;

import com.cedarsoft.utils.Cache;
import com.cedarsoft.utils.HashedCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
  @NotNull
  public <T> GenericDao<T> getDao( @NotNull Class<T> type ) {
    return getDao( type, null );
  }

  @NotNull
  public <T> GenericDao<T> getDao( @NotNull Class<T> type, @Nullable LockProvider<T> lockProvider ) {
    return ( GenericDao<T> ) daoCache.get( new DaoTypeDescriptor<T>( type, lockProvider ) );
  }

  public void shutdown() {
    for ( Iterator<GenericDao<Object>> it = daoCache.values().iterator(); it.hasNext(); ) {
      GenericDao<Object> dao = it.next();
      dao.shutdown();
      it.remove();
    }
  }
}
