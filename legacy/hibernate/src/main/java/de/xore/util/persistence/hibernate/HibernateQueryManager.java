package de.xore.util.persistence.hibernate;


import de.xore.util.persistence.DatabaseException;
import de.xore.util.persistence.QueryManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 21.01.2004
 * Time: 00:27:29
 *
 */

/**
 * <p/>
 * Date: 21.01.2004<br> Time: 00:27:29<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class HibernateQueryManager extends QueryManager<Session> {
  public HibernateQueryManager( @NotNull HibernateDatabaseConnector connector ) {
    super( connector );
  }

  @Override
  @NotNull
  public <T> List<T> getQueryResult( @NotNull String queryString ) throws DatabaseException {
    return getQueryResult( queryString, null );
  }

  @Override
  @NotNull
  public <T> List<T> getQueryResult( @NotNull String queryString, @Nullable Properties params ) throws DatabaseException {
    Session session = connector.getSession();
    Transaction tx = session.beginTransaction();

    Query query = session.createQuery( queryString );
    setParams( query, params );

    List<T> items = query.list();

    tx.commit();
    return items;
  }

  @Override
  @NotNull
  public <T> List<T> getQueryResult( @NotNull String queryString, @NotNull String property, @NotNull String value ) throws DatabaseException {
    Properties properties = new Properties();
    properties.setProperty( property, value );
    return getQueryResult( queryString, properties );
  }

  private static void setParams( @NotNull Query query, @Nullable Properties params ) {
    if ( params == null ) {
      return;
    }
    Enumeration<?> enumeration = params.propertyNames();
    while ( enumeration.hasMoreElements() ) {
      String propertyName = ( String ) enumeration.nextElement();
      String value = params.getProperty( propertyName );
      query.setString( propertyName, value );
    }
  }
}
