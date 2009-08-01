package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsyncCallSupport;
import com.cedarsoft.async.CallbackCaller;
import com.cedarsoft.gdao.GenericService;
import com.cedarsoft.gdao.GenericServiceManager;
import com.cedarsoft.utils.Cache;
import com.cedarsoft.utils.HashedCache;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 *
 */
public class AsynchronousServiceManager implements GenericServiceManager {
  @NotNull
  private final AsyncCallSupport<AsyncServiceCreator<?>> asyncCallSupport = new AsyncCallSupport<AsyncServiceCreator<?>>();

  @SuppressWarnings( {"MismatchedQueryAndUpdateOfCollection"} )
  @NotNull
  private final Cache<Class<Object>, AsynchronousService<Object>> serviceCache = new HashedCache<Class<Object>, AsynchronousService<Object>>(
          new Cache.Factory<Class<Object>, AsynchronousService<Object>>() {
            @NotNull
            public AsynchronousService<Object> create( @NotNull Class<Object> key ) {
              return asyncCallSupport.invoke( new AsyncServiceCreator<Object>( key ) );
            }
          }
  );

  public AsynchronousServiceManager( @NotNull final String description, @NotNull final GenericServiceManager delegatingManager ) {
    asyncCallSupport.initializeWorker( new CallbackCaller<AsyncServiceCreator<?>>() {
      @NotNull
      public String getDescription() {
        return description;
      }

      public Object call( @NotNull AsyncServiceCreator<?> callback ) throws Exception {
        return callback.execute( delegatingManager );
      }
    } );
  }

  @NotNull
  public <T> GenericService<T> getService( @NotNull Class<T> type ) {
    return ( GenericService<T> ) serviceCache.get( type );
  }

  public void shutdown() {
    for ( Iterator<AsynchronousService<Object>> it = serviceCache.values().iterator(); it.hasNext(); ) {
      AsynchronousService<Object> service = it.next();
      service.shutdown();
      it.remove();
    }
    asyncCallSupport.shutdown();
  }

  private static class AsyncServiceCreator<T> {
    @NotNull
    private final Class<T> key;

    private AsyncServiceCreator( @NotNull Class<T> key ) {
      this.key = key;
    }

    @NotNull
    public AsynchronousService<T> execute( @NotNull GenericServiceManager delegatingManager ) {
      GenericService<T> service = delegatingManager.getService( key );
      AsynchronousService<T> asynchronousService = new AsynchronousService<T>();
      asynchronousService.initializeDelegatingService( service );
      return asynchronousService;
    }
  }
}
