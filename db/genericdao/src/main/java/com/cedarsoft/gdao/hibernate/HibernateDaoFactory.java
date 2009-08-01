package com.cedarsoft.gdao.hibernate;

import com.cedarsoft.gdao.DaoTypeDescriptor;
import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.utils.Cache;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Creates Hibernate DAOs if needed.
 */
public class HibernateDaoFactory implements Cache.Factory<DaoTypeDescriptor<Object>, GenericDao<Object>> {
  @NotNull
  private final SessionFactory sessionFactory;

  /**
   * Creates a new factory for the given session factory
   *
   * @param sessionFactory the session factory
   */
  public HibernateDaoFactory( @NotNull SessionFactory sessionFactory ) {
    this.sessionFactory = sessionFactory;
  }

  @NotNull
  public GenericDao<Object> create( @NotNull DaoTypeDescriptor<Object> key ) {
    return new HibernateDao<Object>( sessionFactory, key.getType(), key.getLockProvider() );
  }
}
