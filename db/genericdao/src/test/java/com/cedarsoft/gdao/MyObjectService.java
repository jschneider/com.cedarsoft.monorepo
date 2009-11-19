package com.cedarsoft.gdao;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.Override;

/**
 *
 */
public class MyObjectService extends AbstractService<MyObject> {
  protected MyObjectService( @NotNull GenericDao<MyObject> myObjectLongGenericDao, @NotNull HibernateTransactionManager transactionManager ) {
    super( myObjectLongGenericDao, transactionManager );
  }

  public void changeAllNames( @NotNull @NonNls final String newName ) {
    getTransactionTemplate().execute( new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult( TransactionStatus status ) {
        for ( MyObject myObject : findAll() ) {
          myObject.setName( newName );
          saveOrUpdate( myObject );
        }
      }
    } );
  }
}
