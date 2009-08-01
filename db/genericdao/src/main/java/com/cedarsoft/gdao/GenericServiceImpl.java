package com.cedarsoft.gdao;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;

/**
 * A generic service that can be used generally.
 */
public class GenericServiceImpl<T> extends AbstractService<T> implements GenericService<T> {
  /**
   * Creates a new Generic service implementation
   *
   * @param dao                the dao
   * @param transactionManager the transaction manager
   */
  public GenericServiceImpl( @NotNull GenericDao<T> dao, @NotNull AbstractPlatformTransactionManager transactionManager ) {
    super( dao, transactionManager );
  }

  /**
   * Calls the given callback within an transaction.
   *
   * @param callback the callback
   * @return the return value (optional)
   */
  @Nullable
  public <R> R perform( @NotNull final GenericService.ServiceCallback<T, R> callback ) {
    Object o = transactionTemplate.execute( new TransactionCallback() {
      @Nullable
      public Object doInTransaction( TransactionStatus status ) {
        return callback.perform( GenericServiceImpl.this );
      }
    } );
    return ( R ) o;
  }
}
