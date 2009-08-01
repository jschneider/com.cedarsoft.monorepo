package com.cedarsoft.gdao;

import com.cedarsoft.utils.Cache;
import com.cedarsoft.utils.HashedCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

/**
 * Manages the services
 */
public class DefaultGenericServiceManager implements GenericServiceManager {
  @NotNull
  private final GenericDaoManager daoManager;
  @NotNull
  private final AbstractPlatformTransactionManager transactionManager;

  @SuppressWarnings( {"MismatchedQueryAndUpdateOfCollection"} )
  @NotNull
  private final Cache<Class<Object>, GenericService<Object>> serviceCache;

  /**
   * Should only be necessary for testing purposes
   *
   * @param transactionManager the transaction manager
   * @param daoManager         the dao manager
   * @param factory            the factory
   */
  protected DefaultGenericServiceManager( @NotNull AbstractPlatformTransactionManager transactionManager, @NotNull GenericDaoManager daoManager, @NotNull Cache.Factory<Class<Object>, GenericService<Object>> factory ) {
    serviceCache = new HashedCache<Class<Object>, GenericService<Object>>( factory );
    this.transactionManager = transactionManager;
    this.daoManager = daoManager;

  }

  /**
   * Creates a new service manager
   *
   * @param transactionManager the transaction manager
   * @param daoManager         the dao manager
   */
  public DefaultGenericServiceManager( @NotNull AbstractPlatformTransactionManager transactionManager, @NotNull GenericDaoManager daoManager ) {
    this.transactionManager = transactionManager;
    this.daoManager = daoManager;
    serviceCache = new HashedCache<Class<Object>, GenericService<Object>>( new Cache.Factory<Class<Object>, GenericService<Object>>() {
      @NotNull
      public GenericService<Object> create( @NotNull Class<Object> key ) {
        return new GenericServiceImpl<Object>( DefaultGenericServiceManager.this.daoManager.getDao( key ), DefaultGenericServiceManager.this.transactionManager );
      }
    } );
  }

  /**
   * Returns the service for the given type
   *
   * @param type the type
   * @return the service for the given type
   */
  @NotNull
  public <T> GenericService<T> getService( @NotNull Class<T> type ) {
    return ( GenericService<T> ) serviceCache.get( type );
  }
}
