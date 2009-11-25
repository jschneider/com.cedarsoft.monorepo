package de.xore.util.persistence;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A
 * <p/>
 * <p/>
 * Date: 19.05.2005<br>
 * Time: 00:01:00<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 *         <p/>
 *         <p/>
 *         T: The session, e.g. org.hibernate.Session
 */
public interface DatabaseConnector<T> {
  void addAnnotadedClass( @NotNull Class<?> annClass );

  void recreateDb() throws DatabaseException;

  void beginTransaction() throws DatabaseException;

  void closeSession() throws DatabaseException;

  void commit() throws DatabaseException;

  void delete( @NotNull Object object ) throws DatabaseException;

  @NotNull
  <T> T load( @NotNull Class<T> aClass, Serializable id ) throws DatabaseException;

  void rollback() throws DatabaseException;

  void save( @NotNull Object object ) throws DatabaseException;

  @NotNull
  T getSession() throws DatabaseException;

  void initSession() throws DatabaseException;

  void refresh( @NotNull Object object ) throws DatabaseException;

  void update( @NotNull Object object ) throws DatabaseException;

  void saveOrUpdate( @NotNull Object object ) throws DatabaseException;

  boolean isSessionActive();

  void dispose();
}