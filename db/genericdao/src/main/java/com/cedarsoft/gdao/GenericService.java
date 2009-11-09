package com.cedarsoft.gdao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.Override;

/**
 * A generic service that extends the behaviour of simple DAOs with transaction support.
 */
public interface GenericService<T> extends GenericDao<T> {
  /**
   * Performs an action within an transaction.
   *
   * @param callback the callback that is called within a transaction
   * @return the return value (optional)
   */
  @Nullable
      <R> R perform( @NotNull ServiceCallback<T, R> callback );


  /**
   * A callback that can be used to be invoked within a service (in a transaction).
   */
  interface ServiceCallback<T, R> {
    /**
     * Perform the action
     *
     * @param service the service
     * @return the return value (may be null)
     */
    @Nullable
    R perform( @NotNull GenericService<T> service );
  }

  /**
   * Abstract base class for service callbacks without return value.
   */
  abstract class ServiceCallbackWithoutReturnValue<T> implements ServiceCallback<T, Object> {
    @Override
    @Nullable
    public Object perform( @NotNull GenericService<T> service ) {
      performVoid( service );
      return null;
    }

    /**
     * Perform the action without any return value
     *
     * @param service the service
     */
    protected abstract void performVoid( @NotNull GenericService<T> service );
  }
}

