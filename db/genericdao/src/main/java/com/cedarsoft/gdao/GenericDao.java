package com.cedarsoft.gdao;

import com.cedarsoft.CommitableObjectAccess;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

/**
 * A generic dao. This implementation expects a Long as PK
 *
 * @param <T> the type this dao can manage
 */
public interface GenericDao<T> extends CommitableObjectAccess<T> {
  /**
   * Saves the newInstance object into database
   */
  @NotNull
          <LT extends T> Long save( @NotNull LT newInstance ) throws DataAccessException;

  /**
   * Save changes made to a persistent object.
   */
  <LT extends T> void update( @NotNull LT transientObject ) throws DataAccessException;

  /**
   * Stores the changes to a persistent object or saves it
   *
   * @param object the object
   */
  <LT extends T> void saveOrUpdate( @NotNull LT object ) throws DataAccessException;

  /**
   * Remove an object from persistent storage in the database
   */
  <LT extends T> void delete( @NotNull LT persistentObject ) throws DataAccessException;

  /**
   * Retrieve an object that was previously persisted to the database using
   * the indicated id as primary key
   *
   * @param id the id of the bean that is loaded
   */
  @NotNull
  T findById( @NotNull Long id ) throws EmptyResultDataAccessException;

  /**
   * Finds an object.
   *
   * @param finder the selector
   * @return the selected objects
   *
   * @throws EmptyResultDataAccessException if nothing has been found
   */
  @NotNull
          <R> R find( @NotNull Finder<R> finder ) throws EmptyResultDataAccessException;

  /**
   * Selects all objects
   *
   * @return all objects
   */
  @NotNull
  List<? extends T> findAll() throws DataAccessException;

  /**
   * Returns the count
   *
   * @return the count
   */
  int getCount() throws DataAccessException;

  /**
   * Shuts the dao down
   */
  void shutdown();
}
