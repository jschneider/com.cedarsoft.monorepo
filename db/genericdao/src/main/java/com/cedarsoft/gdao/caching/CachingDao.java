package com.cedarsoft.gdao.caching;

import com.cedarsoft.gdao.AbstractGenericDao;
import com.cedarsoft.gdao.Finder;
import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.history.ClusteredElementsCollection;
import com.cedarsoft.history.ClusteredObservableObjectAccess;
import com.cedarsoft.history.ElementVisitor;
import com.cedarsoft.history.ElementsListener;
import com.cedarsoft.history.NoElementFoundException;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * A dao that caches the objects (in memory) and is backed up by a dao that is called asynchronously.
 * This class is *not* thread safe
 */
public class CachingDao<T> extends AbstractGenericDao<T> implements ClusteredObservableObjectAccess<T> {
  @NotNull
  protected final ClusteredElementsCollection<T> cache = new ClusteredElementsCollection<T>();
  @NotNull
  private final GenericDao<T> backingDao;

  public CachingDao( @NotNull GenericDao<T> backingDao ) {
    this.backingDao = backingDao;
  }

  @NotNull
  public ReadWriteLock getLock() {
    throw new UnsupportedOperationException(); //todo implement(?)
  }

  @NotNull
  public ClusteredElementsCollection<T> getCache() {
    return cache;
  }

  public void initializeCache( @NotNull List<? extends T> elements ) {
    cache.clear();

    for ( T element : elements ) {
      cache.add( element );
    }
  }

  @NotNull
  public GenericDao<T> getBackingDao() {
    return backingDao;
  }

  @NotNull
  public <LT extends T> Long save( @NotNull LT newInstance ) throws DataAccessException {
    ensureNotEDT();

    Long id = backingDao.save( newInstance );
    cache.add( newInstance );
    return id;
  }

  public <LT extends T> void update( @NotNull LT transientObject ) throws DataAccessException {
    ensureNotEDT();

    backingDao.update( transientObject );
    cache.commit( transientObject );
  }

  private static void ensureNotEDT() {
    if ( SwingUtilities.isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "Must not be called from EDT" );
    }
  }

  @Override
  public void commit( @NotNull T element ) {
    update( element );
  }

  public <LT extends T> void saveOrUpdate( @NotNull LT object ) throws DataAccessException {
    ensureNotEDT();

    backingDao.saveOrUpdate( object );
    if ( cache.contains( object ) ) {
      cache.commit( object );
    } else {
      cache.add( object );
    }
  }

  public <LT extends T> void delete( @NotNull LT persistentObject ) throws DataAccessException {
    ensureNotEDT();

    backingDao.delete( persistentObject );
    cache.remove( persistentObject );
  }

  @NotNull
  public T findById( @NotNull Long id ) throws EmptyResultDataAccessException {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public <R> R find( @NotNull final Finder<R> finder ) throws EmptyResultDataAccessException {
    return getBackingDao().find( new Finder<R>() {
      @NotNull
      public R find( @NotNull Session session ) throws DataAccessException {
        ensureNotEDT();

        for ( T t : cache.getElements() ) {
          session.update( t );
        }
        return finder.find( session );
      }
    } );
  }

  @NotNull
  public List<? extends T> findAll() throws DataAccessException {
    return cache.getElements();
  }

  public int getCount() throws DataAccessException {
    return cache.size();
  }

  public void addElementListener( @NotNull ElementsListener<? super T> listener ) {
    cache.addElementListener( listener );
  }

  public void addElementListener( @NotNull ElementsListener<? super T> listener, boolean isTransient ) {
    cache.addElementListener( listener, isTransient );
  }

  public void removeElementListener( @NotNull ElementsListener<? super T> listener ) {
    cache.removeElementListener( listener );
  }

  @NotNull
  public List<? extends ElementsListener<? super T>> getTransientElementListeners() {
    return cache.getTransientElementListeners();
  }

  @NotNull
  public List<? extends T> findElements( @NotNull ElementVisitor<? super T> elementVisitor ) {
    return cache.findElements( elementVisitor );
  }

  @NotNull
  public T findFirstElement( @NotNull ElementVisitor<? super T> elementVisitor ) throws NoElementFoundException {
    return cache.findFirstElement( elementVisitor );
  }

  @Nullable
  public T findFirstElementNullable( @NotNull ElementVisitor<? super T> elementVisitor ) {
    return cache.findFirstElementNullable( elementVisitor );
  }

  public boolean contains( @NotNull T element ) {
    return cache.contains( element );
  }

  public boolean contains( @NotNull ElementVisitor<? super T> elementVisitor ) throws NoElementFoundException {
    return cache.contains( elementVisitor );
  }

  public void shutdown() {
    backingDao.shutdown();
  }
}
