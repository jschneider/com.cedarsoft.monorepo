package com.cedarsoft.gdao.spring;

import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.GenericDaoManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.FactoryBean;

import java.lang.Override;

/**
 * Special factory bean that resolves DAOs for a class
 */
public class DaoFactory<T> implements FactoryBean {
  @NotNull
  private final GenericDaoManager genericDaoManager;

  @NotNull
  private final Class<T> type;

  /**
   * Creates a new factory
   *
   * @param genericDaoManager the manager that is used to resolve the DAOs
   * @param type              the type the dao is searched for
   */
  public DaoFactory( @NotNull GenericDaoManager genericDaoManager, @NotNull Class<T> type ) {
    this.genericDaoManager = genericDaoManager;
    this.type = type;
  }

  @Override
  @NotNull
  public GenericDao<T> getObject() throws Exception {
    return genericDaoManager.getDao( type );
  }

  @Override
  public Class<GenericDao> getObjectType() {
    return GenericDao.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
