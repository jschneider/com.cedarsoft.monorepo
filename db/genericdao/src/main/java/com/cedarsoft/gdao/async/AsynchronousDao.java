package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsyncCallSupport;
import com.cedarsoft.async.AsynchroniousInvocationException;
import com.cedarsoft.async.CallbackCaller;
import com.cedarsoft.gdao.AbstractGenericDao;
import com.cedarsoft.gdao.Finder;
import com.cedarsoft.gdao.GenericDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fest.reflect.core.Reflection;
import org.fest.reflect.exception.ReflectionError;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * An asynchronous dao where the save/loading is done in another thread.
 * You have to call {@link #initializeDelegatingDao(GenericDao, Lock)} to start the
 * worker threads.
 */
public class AsynchronousDao<T> extends AbstractGenericDao<T> {
  @NonNls
  @NotNull
  private static final Log log = LogFactory.getLog( AsynchronousDao.class );

  @NotNull
  protected final AsyncCallSupport<DaoAction<T, ?>> asyncCallSupport = new AsyncCallSupport<DaoAction<T, ?>>();

  /**
   * Initializes the delegating dao with a null lock
   *
   * @param delegatingDao the delegating dao
   */
  public void initializeDelegatingDao( @NotNull final GenericDao<T> delegatingDao ) {
    initializeDelegatingDao( delegatingDao, null );
  }

  /**
   * Sets the backing dao and start the worker thread
   *
   * @param delegatingDao the delegating dao
   * @parramt lock the lock that is used before the callback is executed
   */
  public void initializeDelegatingDao( @NotNull final GenericDao<T> delegatingDao, @Nullable final Lock lock ) {
    log.info( "initializing with " + delegatingDao );
    asyncCallSupport.initializeWorker( new CallbackCaller<DaoAction<T, ?>>() {
      @Override
      @NotNull
      public String getDescription() {
        return "AsynchroniousDao-CallbackCaller for " + delegatingDao;
      }

      @Override
      public Object call( @NotNull DaoAction<T, ?> callback ) throws Exception {
        log.debug( "executing " + callback );
        if ( lock != null ) {
          //noinspection LockAcquiredButNotSafelyReleased
          lock.lock();
        }
        try {
          return callback.execute( delegatingDao );
        } finally {
          if ( lock != null ) {
            lock.unlock();
          }
        }
      }
    } );
  }

  @Override
  @NotNull
  public <LT extends T> Long save( @NotNull final LT newInstance ) {
    return invoke( new DaoAction<T, Long>() {
      @Override
      @NotNull
      public Long execute( @NotNull GenericDao<T> dao ) {
        return dao.save( newInstance );
      }

      @Override
      public String toString() {
        return "saving " + newInstance.getClass().getName() + ": " + newInstance + " with id " + resolveId( newInstance );
      }
    } );
  }

  @Nullable
  protected Object resolveId( @NotNull Object object ) {
    try {
      return Reflection.field( "id" ).ofType( Long.class ).in( object ).get();
    } catch ( ReflectionError ignore ) {
      ignore.printStackTrace();
    }

    return null;
  }

  @Override
  public <LT extends T> void update( @NotNull final LT transientObject ) {
    invokeVoid( new VoidDaoAction<T>() {
      @Override
      public void executeVoid( @NotNull GenericDao<T> dao ) {
        dao.update( transientObject );
      }

      @Override
      public String toString() {
        return "Updating " + transientObject.getClass().getName() + ": " + transientObject + " with id " + resolveId( transientObject );
      }
    } );
  }

  @Override
  public <LT extends T> void saveOrUpdate( @NotNull final LT object ) {
    invokeVoid( new VoidDaoAction<T>() {
      @Override
      public void executeVoid( @NotNull GenericDao<T> dao ) {
        dao.saveOrUpdate( object );
      }

      @Override
      public String toString() {
        return "SaveOrUpdate " + object.getClass().getName() + ": " + object + " with id " + resolveId( object );
      }
    } );
  }

  @Override
  public <LT extends T> void delete( @NotNull final LT persistentObject ) {
    invokeVoid( new VoidDaoAction<T>() {
      @Override
      public void executeVoid( @NotNull GenericDao<T> dao ) {
        dao.delete( persistentObject );
      }

      @Override
      public String toString() {
        return "Deleting " + persistentObject.getClass().getName() + " " + persistentObject + " with id " + resolveId( persistentObject );
      }
    } );
  }

  @Override
  @NotNull
  public T findById( @NotNull final Long id ) {
    return invoke( new DaoAction<T, T>() {
      @Override
      @NotNull
      public T execute( @NotNull GenericDao<T> dao ) {
        return dao.findById( id );
      }

      @Override
      public String toString() {
        return "findById " + id;
      }
    } );
  }

  @Override
  @NotNull
  public <R> R find( @NotNull final Finder<R> finder ) {
    return invoke( new DaoAction<T, R>() {
      @Override
      @NotNull
      public R execute( @NotNull GenericDao<T> dao ) {
        return dao.find( finder );
      }

      @Override
      public String toString() {
        return "find with " + finder;
      }
    } );
  }

  @Override
  @NotNull
  public List<? extends T> findAll() {
    return invoke( new DaoAction<T, List<? extends T>>() {
      @Override
      public List<? extends T> execute( @NotNull GenericDao<T> dao ) {
        return dao.findAll();
      }

      @Override
      public String toString() {
        return "finding All";
      }
    } );
  }

  @Override
  public int getCount() {
    return invoke( new DaoAction<T, Integer>() {
      @Override
      public Integer execute( @NotNull GenericDao<T> dao ) {
        return dao.getCount();
      }

      @Override
      public String toString() {
        return "getting count";
      }
    } );
  }

  /**
   * Invokes an action
   *
   * @param action the action
   * @return the return value of the action
   */
  @NotNull
  private <R> R invoke( @NotNull DaoAction<T, R> action ) throws AsynchroniousInvocationException {
    return asyncCallSupport.<R>invoke( action );
  }

  /**
   * Invokes an action
   *
   * @param actionVoid the action
   * @throws AsynchroniousInvocationException
   *
   */
  private void invokeVoid( @NotNull VoidDaoAction<T> actionVoid ) throws AsynchroniousInvocationException {
    asyncCallSupport.invokeVoid( actionVoid );
  }

  @Override
  public void shutdown() {
    asyncCallSupport.shutdown();
  }

  /**
   * A action that doesn't have a return value
   */
  public abstract static class VoidDaoAction<T> implements DaoAction<T, Object> {
    @Override
    @Nullable
    public Object execute( @NotNull GenericDao<T> dao ) {
      executeVoid( dao );
      return null;
    }

    /**
     * Execute the action without any return value
     *
     * @param dao the dao
     */
    public abstract void executeVoid( @NotNull GenericDao<T> dao );
  }

  /**
   * An action that has an return value
   */
  public static interface DaoAction<T, R> {
    /**
     * Executes the action
     *
     * @param dao the dao
     * @return the return value
     */
    @Nullable
    R execute( @NotNull GenericDao<T> dao );
  }
}
