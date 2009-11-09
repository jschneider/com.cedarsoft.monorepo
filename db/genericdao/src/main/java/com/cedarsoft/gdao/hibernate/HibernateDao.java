package com.cedarsoft.gdao.hibernate;

import com.cedarsoft.NullLock;
import com.cedarsoft.gdao.Finder;
import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.LockProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.lang.Override;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * Hibernate implementation for {@link GenericDao}.
 * This class is *not* thread safe
 *
 * @param <T>the type
 */
public class HibernateDao<T> extends HibernateDaoSupport implements GenericDao<T> {
  @NonNls
  @NotNull
  private static final Log log = LogFactory.getLog( HibernateDao.class );

  @NotNull
  private final Class<T> type;
  @Nullable
  private final LockProvider<T> lockProvider;

  /**
   * Creates a new instance
   *
   * @param sessionFactory the session factory
   * @param type           the type
   */
  public HibernateDao( @NotNull SessionFactory sessionFactory, @NotNull Class<T> type ) {
    this( sessionFactory, type, null );
  }

  public HibernateDao( @NotNull SessionFactory sessionFactory, @NotNull Class<T> type, @Nullable LockProvider<T> lockProvider ) {
    this.type = type;
    this.lockProvider = lockProvider;
    setSessionFactory( sessionFactory );
  }

  @Nullable
  public LockProvider<T> getLockProvider() {
    return lockProvider;
  }

  @Override
  public final void remove( @NotNull T element ) throws DataAccessException {
    delete( element );
  }

  @Override
  public final void add( @NotNull T element ) {
    saveOrUpdate( element );
  }

  @Override
  public void commit( @NotNull T element ) {
    update( element );
  }

  @Override
  public void setElements( @NotNull List<? extends T> elements ) {
    throw new UnsupportedOperationException( "Not supported for daos" );
  }

  @Override
  @NotNull
  public final List<? extends T> getElements() {
    return findAll();
  }

  @Override
  @NotNull
  public List<? extends T> findAll() {
    return getHibernateTemplate().executeFind( new HibernateCallback() {
      @Override
      public Object doInHibernate( Session session ) throws HibernateException {
        return session.createCriteria( type ).list();
      }
    } );
  }

  @Override
  public int getCount() {
    return ( Integer ) getHibernateTemplate().execute( new HibernateCallback() {
      @Override
      public Object doInHibernate( Session session ) throws HibernateException {
        return session.createCriteria( type ).list().size();
      }
    } );
  }

  @Override
  @NotNull
  public <LT extends T> Long save( @NotNull LT newInstance ) {
    Lock writeLock = getWriteLock( newInstance );
    writeLock.lock();
    try {
      return ( Long ) getHibernateTemplate().save( newInstance );
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Returns the write lock for the given object
   *
   * @param object the object the write lock is obtained for
   * @return the write lock for the given object or a NULL lock
   */
  @NotNull
  private Lock getWriteLock( @NotNull T object ) {
    if ( lockProvider != null ) {
      return lockProvider.getWriteLock( object );
    } else {
      return NullLock.LOCK;
    }
  }

  @Override
  @NotNull
  public T findById( @NotNull Long id ) throws DataAccessException {
    T found = type.cast( getHibernateTemplate().get( type, id ) );
    if ( found == null ) {
      throw new EmptyResultDataAccessException( 1 );
    }
    return found;
  }

  @Override
  public <LT extends T> void saveOrUpdate( @NotNull LT object ) {
    Lock lock = getWriteLock( object );
    lock.lock();
    try {
      getHibernateTemplate().saveOrUpdate( object );
    } finally {
      lock.unlock();
    }
  }

  @Override
  public <LT extends T> void update( @NotNull LT transientObject ) {
    Lock lock = getWriteLock( transientObject );
    lock.lock();
    try {
      getHibernateTemplate().saveOrUpdate( transientObject );
    } finally {
      lock.unlock();
    }
  }

  @Override
  public <LT extends T> void delete( @NotNull LT persistentObject ) throws DataAccessException {
    Lock lock = getWriteLock( persistentObject );
    lock.lock();
    try {
      getHibernateTemplate().delete( persistentObject );
    } catch ( DataAccessException e ) {
      log.warn( "Error when trying to delete " + persistentObject );
      throw e;
    } finally {
      lock.unlock();
    }
  }

  @Override
  @NotNull
  public <T> T find( @NotNull final Finder<T> finder ) throws EmptyResultDataAccessException {
    //noinspection unchecked
    return ( T ) getHibernateTemplate().execute( new HibernateCallback() {
      @Override
      @NotNull
      public T doInHibernate( @NotNull Session session ) throws HibernateException {
        return finder.find( session );
      }
    } );
  }

  @Override
  public void shutdown() {
  }

  @Override
  public String toString() {
    return "HibernateDao for: " + type.getName() + ". " + hashCode();
  }
}
