package com.cedarsoft.gdao.caching;

import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.GenericDaoManager;
import com.cedarsoft.gdao.LockProvider;
import com.cedarsoft.utils.Cache;
import com.cedarsoft.utils.HashedCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.util.Iterator;

/**
 *
 */
public class CachingDaoManager implements GenericDaoManager {
  @NotNull
  private final GenericDaoManager backingDaoManager;

  @SuppressWarnings( {"MismatchedQueryAndUpdateOfCollection"} )
  private final Cache<Class<Object>, CachingDao<Object>> daoCache = new HashedCache<Class<Object>, CachingDao<Object>>( new Cache.Factory<Class<Object>, CachingDao<Object>>() {
    @Override
    @NotNull
    public CachingDao<Object> create( @NotNull Class<Object> key ) {
      GenericDao<Object> backingDao = backingDaoManager.getDao( key );
      CachingDao<Object> dao = new CachingDao<Object>( backingDao );
      dao.initializeCache( backingDao.findAll() );
      return dao;
    }
  } );

  public CachingDaoManager( @NotNull GenericDaoManager backingDaoManager ) {
    this.backingDaoManager = backingDaoManager;
  }

  @Override
  @NotNull
  public <T> GenericDao<T> getDao( @NotNull Class<T> type ) {
    return ( GenericDao<T> ) daoCache.get( type );
  }

  @Override
  @NotNull
  public <T> GenericDao<T> getDao( @NotNull Class<T> type, @Nullable LockProvider<T> lockProvider ) {
    if ( lockProvider == null ) {
      return getDao( type );
    } else {
      throw new UnsupportedOperationException( "Lock provider not supported" );
    }
  }

  @Override
  public void shutdown() {
    for ( Iterator<CachingDao<Object>> it = daoCache.values().iterator(); it.hasNext(); ) {
      CachingDao<Object> dao = it.next();
      dao.shutdown();
      it.remove();
    }
    backingDaoManager.shutdown();
  }
}
