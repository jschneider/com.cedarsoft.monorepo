package com.cedarsoft.gdao.spring;

import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.GenericService;
import com.cedarsoft.gdao.GenericServiceManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 */
@Deprecated
public class ServiceFactory<T> implements FactoryBean {
  @NotNull
  private final GenericServiceManager genericServiceManager;

  @NotNull
  private final Class<T> type;

  /**
   * Creates a new factory
   *
   * @param genericServiceManager the manager that is used to resolve the DAOs
   * @param type                  the type the dao is searched for
   */
  public ServiceFactory( @NotNull GenericServiceManager genericServiceManager, @NotNull Class<T> type ) {
    this.genericServiceManager = genericServiceManager;
    this.type = type;
  }

  @NotNull
  public GenericDao<T> getObject() throws Exception {
    return genericServiceManager.getService( type );
  }

  public Class<GenericService> getObjectType() {
    return GenericService.class;
  }

  public boolean isSingleton() {
    return true;
  }
}
