package com.cedarsoft.gdao;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Abstract base class for services.
 */
public abstract class AbstractService<T> extends AbstractGenericDao<T> {
  @NotNull
  protected final TransactionTemplate transactionTemplate;
  @NotNull
  protected final GenericDao<T> dao;

  /**
   * Creates a new service
   *
   * @param dao                the dao
   * @param transactionManager the transaction manager that is used
   */
  protected AbstractService( @NotNull GenericDao<T> dao, @NotNull AbstractPlatformTransactionManager transactionManager ) {
    this.dao = dao;
    this.transactionTemplate = new TransactionTemplate( transactionManager );
  }

  @NotNull
  protected TransactionTemplate getTransactionTemplate() {
    return transactionTemplate;
  }

  /**
   * Returns the underlying dao
   *
   * @return the dao
   */
  @NotNull
  public GenericDao<T> getDao() {
    return dao;
  }

  @NotNull
  public List<? extends T> findAll() {
    return dao.findAll();
  }

  public int getCount() {
    return dao.getCount();
  }

  @NotNull
  public <LT extends T> Long save( @NotNull LT newInstance ) {
    return dao.save( newInstance );
  }

  @NotNull
  public T findById( @NotNull Long id ) {
    return dao.findById( id );
  }

  public <LT extends T> void update( @NotNull LT transientObject ) {
    dao.update( transientObject );
  }

  public <LT extends T> void saveOrUpdate( @NotNull LT transientObject ) {
    dao.saveOrUpdate( transientObject );
  }

  public <LT extends T> void delete( @NotNull LT persistentObject ) {
    dao.delete( persistentObject );
  }

  @NotNull
  public <T> T find( @NotNull Finder<T> finder ) {
    return dao.find( finder );
  }

  public void shutdown() {
    dao.shutdown();
  }
}
