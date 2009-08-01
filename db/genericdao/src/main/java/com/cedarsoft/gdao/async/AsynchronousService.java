package com.cedarsoft.gdao.async;

import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.GenericService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class AsynchronousService<T> extends AsynchronousDao<T> implements GenericService<T> {
  @Nullable
  public <R> R perform( @NotNull final ServiceCallback<T, R> callback ) {
    return asyncCallSupport.<R>invokeNullable( new DaoAction<T, R>() {
      public R execute( @NotNull GenericDao<T> dao ) {
        GenericService<T> service = ( GenericService<T> ) dao;
        return callback.perform( service );
      }
    } );
  }

  public void initializeDelegatingService( @NotNull GenericService<T> delegatingService ) {
    initializeDelegatingDao( delegatingService );
  }

  @Override
  public void initializeDelegatingDao( @NotNull GenericDao<T> delegatingDao ) {
    if ( !( delegatingDao instanceof GenericService ) ) {
      throw new IllegalArgumentException( "AsynchronousService must be initialized with an GenericService as delegating dao" );
    }
    super.initializeDelegatingDao( delegatingDao );
  }
}
