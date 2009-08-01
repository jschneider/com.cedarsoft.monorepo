package de.xore.util.persistence;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Properties;

/**
 * Soll/kann von den einzelnen Modulen ueberschrieben werden - mit den entsprechenden Queries <p>
 * Date: 14.04.2005<br>
 * Time: 14:38:17<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public abstract class QueryManager<T> {
  protected DatabaseConnector<T> connector;

  protected QueryManager( @NotNull DatabaseConnector<T> connector ) {
    this.connector = connector;
  }

  public DatabaseConnector<T> getConnector() {
    return connector;
  }

  public void refresh( Object obj ) {
    connector.beginTransaction();
    connector.refresh( obj );
    connector.commit();
  }

  /**
   * Update the database
   *
   * @param obj the object that is saved
   */
  public void update( Object obj ) {
    connector.beginTransaction();
    connector.update( obj );
    connector.commit();
  }

  public void save( @NotNull Object object ) {
    connector.beginTransaction();
    connector.save( object );
    connector.commit(); //todo wirklich automatisch committen??
  }

  public void saveOrUpdate( @NotNull Object object ) {
    connector.beginTransaction();
    connector.saveOrUpdate( object );
    connector.commit(); //todo wirklich automatisch committen??
  }

  public void delete( @NotNull Object object ) {
    connector.beginTransaction();
    connector.delete( object );
    connector.commit();
  }

  @Nullable
  public <T> T getFirstQueryResult( String queryString ) throws DatabaseException {
    List<T> queryResult = getQueryResult( queryString );
    if ( !queryResult.isEmpty() ) {
      return queryResult.get( 0 );
    }
    return null;
  }

  @Nullable
  public <T> T getFirstQueryResult( String queryString, Properties params ) throws DatabaseException {
    List<T> queryResult = getQueryResult( queryString, params );
    if ( !queryResult.isEmpty() ) {
      return queryResult.get( 0 );
    }
    return null;
  }

  @Nullable
  public <T> T getFirstQueryResult( String queryString, String name, String val ) throws DatabaseException {
    List<T> queryResult = getQueryResult( queryString, name, val );
    if ( !queryResult.isEmpty() ) {
      return queryResult.get( 0 );
    }
    return null;
  }

  public abstract <T> List<T> getQueryResult( String queryString ) throws DatabaseException;

  public abstract <T> List<T> getQueryResult( String queryString, Properties params ) throws DatabaseException;

  public abstract <T> List<T> getQueryResult( String queryString, String property, String value ) throws DatabaseException;

  public void dispose() {
    getConnector().dispose();
  }
}


