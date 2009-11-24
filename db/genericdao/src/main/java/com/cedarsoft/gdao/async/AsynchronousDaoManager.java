package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsyncCallSupport;
import com.cedarsoft.async.CallbackCaller;
import com.cedarsoft.cache.Cache;
import com.cedarsoft.cache.HashedCache;
import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.GenericDaoManager;
import com.cedarsoft.gdao.LockProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.util.Iterator;

/**
 * Special implementation of {@link GenericDaoManager} that
 * returns {@link AsynchronousDao}s.
 */
public final class AsynchronousDaoManager implements GenericDaoManager {
  @NonNls
  @NotNull
  private static final Log log = LogFactory.getLog( AsynchronousDaoManager.class );
  @NotNull
  private final AsyncCallSupport<AsyncDaoCreator<?>> asyncCallSupport = new AsyncCallSupport<AsyncDaoCreator<?>>();

  @SuppressWarnings( {"MismatchedQueryAndUpdateOfCollection"} )
  @NotNull
  private final Cache<Class<Object>, AsynchronousDao<Object>> daoCache = new HashedCache<Class<Object>, AsynchronousDao<Object>>(
      new Cache.Factory<Class<Object>, AsynchronousDao<Object>>() {
        @Override
        @NotNull
        public AsynchronousDao<Object> create( @NotNull Class<Object> key ) {
          return asyncCallSupport.invoke( new AsyncDaoCreator<Object>( key ) );
        }
      }
  );

  public AsynchronousDaoManager( @NotNull final String description, @NotNull final GenericDaoManager delegatingManager ) {
    asyncCallSupport.initializeWorker( new CallbackCaller<AsyncDaoCreator<?>>() {
      @Override
      public Object call( @NotNull AsyncDaoCreator<?> callback ) throws Exception {
        log.debug( "executing callback" );
        return callback.execute( delegatingManager );
      }

      @Override
      @NotNull
      public String getDescription() {
        return description;
      }
    } );
  }

  /**
   * Returns the asynchronous Dao
   *
   * @param type the type
   * @return the asynchronous dao for the given type
   */
  @Override
  @NotNull
  public <T> AsynchronousDao<T> getDao( @NotNull Class<T> type ) {
    return ( AsynchronousDao<T> ) daoCache.get( type );
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
    for ( Iterator<AsynchronousDao<Object>> it = daoCache.values().iterator(); it.hasNext(); ) {
      AsynchronousDao<Object> dao = it.next();
      dao.shutdown();
      it.remove();
    }

    asyncCallSupport.shutdown();
  }

  private static class AsyncDaoCreator<T> {
    @NotNull
    private final Class<T> key;

    private AsyncDaoCreator( @NotNull Class<T> key ) {
      this.key = key;
    }

    @NotNull
    public AsynchronousDao<T> execute( @NotNull GenericDaoManager delegatingManager ) {
      log.debug( "Creating a new asynchronious dao for " + key.getName() );
      GenericDao<T> dao = delegatingManager.getDao( key );
      AsynchronousDao<T> asynchronousDao = new AsynchronousDao<T>();
      asynchronousDao.initializeDelegatingDao( dao );
      return asynchronousDao;
    }
  }
}
