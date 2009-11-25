/*
 * Copyright (c) 2005 Xore Systems. All Rights Reserved.
 */
package de.xore.util.persistence.hibernate;

import de.xore.util.persistence.DatabaseConfiguration;
import de.xore.util.persistence.DatabaseConnector;
import de.xore.util.persistence.DatabaseException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.FirebirdDialect;
import org.hibernate.dialect.MySQLDialect;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Diese Klasse dient dazu, die Verbindungen zu der Datenbank entsprechend zu kapseln. Es sollte moeglichst wenig
 * Hibernate-Spezifisches Zeugs in den Klassen zu finden sein.
 * <p/>
 * <p> Date: 19.04.2005<br> Time: 22:34:09<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class HibernateDatabaseConnector implements DatabaseConnector<Session> {
  @NotNull
  public static Class<? extends Dialect> getDialect( @NotNull DatabaseConfiguration configuration ) {
    switch ( configuration.getDatabaseType() ) {
      case MYSQL:
        return MySQLDialect.class;
      case FIREBIRD:
        return FirebirdDialect.class;
    }
    throw new IllegalArgumentException( "Did not find a valid Dialect for type " + configuration.getDatabaseType() );
  }

  private SessionFactory sessionFactory;
  private AnnotationConfiguration annotationConfiguration;
  private DatabaseConfiguration configuration;
  private Transaction transaction;
  private Session session;

  public HibernateDatabaseConnector( DatabaseConfiguration configuration ) {
    this.configuration = configuration;
    if ( !configuration.isValid() ) {
      throw new IllegalArgumentException( "DatabaseConfiguration is not valid: " + configuration );
    }
    annotationConfiguration = new AnnotationConfiguration();

    annotationConfiguration.setProperty( Environment.USER, configuration.getUser() );
    annotationConfiguration.setProperty( Environment.PASS, configuration.getPass() );
    annotationConfiguration.setProperty( Environment.URL, configuration.getUrl() );
    annotationConfiguration.setProperty( Environment.DRIVER, configuration.getDriverClassName() );
    //annotationConfiguration.setProperty( Environment.SHOW_SQL, "false" );

    annotationConfiguration.setProperty( Environment.DIALECT, getDialect( configuration ).getName() );
  }

  public void beginTransaction() throws DatabaseException {
    transaction = getSession().beginTransaction();
  }

  public void closeSession() {
    if ( session == null ) {
      return;
    }
    session.close();
    //noinspection AssignmentToNull
    session = null;
  }

  public void commit() {
    if ( transaction == null || !transaction.isActive() ) {
      throw new IllegalStateException( "Start transaction first" );
    }
    transaction.commit();
  }

  public void delete( @NotNull Object object ) {
    getSession().delete( object );
  }

  @NotNull
  public <T> T load( @NotNull Class<T> aClass, Serializable id ) {
    return aClass.cast( getSession().load( aClass, id ) );
  }

  public void rollback() {
    if ( transaction != null ) {
      transaction.rollback();
    }
  }

  public void update( @NotNull Object object ) {
    getSession().update( object );
  }

  public void saveOrUpdate( @NotNull Object object ) {
    getSession().saveOrUpdate( object );
  }

  public void save( @NotNull Object object ) {
    getSession().save( object );
  }

  public void saveChanges( @NotNull Object object ) {
    getSession().update( object );
  }

  public boolean isSessionActive() {
    return session != null && session.isOpen();
  }

  public void dispose() {
    if ( session != null ) {
      session.close();
    }
    if ( sessionFactory != null ) {
      sessionFactory.close();
    }
  }

  @NotNull
  public Session getSession() {
    if ( session == null ) {
      initSession();
    }
    return session;
  }

  public void initSession() {
    openSession();
  }

  public void refresh( @NotNull Object object ) {
    getSession().refresh( object );
  }

  public void addAllAnnotdedClass( @NotNull List<Class<?>> persistentClasses ) {
    for ( Class<?> aClass : persistentClasses ) {
      addAnnotadedClass( aClass );
    }
  }

  public void addAnnotadedClass( @NotNull Class<?> annClass ) {
    annotationConfiguration.addAnnotatedClass( annClass );
  }

  @NotNull
  public Session openSession() throws HibernateException {
    if ( sessionFactory == null ) {
      sessionFactory = annotationConfiguration.buildSessionFactory();
    }

    session = sessionFactory.openSession();
    if ( session == null ) {
      throw new DatabaseException( "could not create session" );
    }
    return session;
  }

  public void recreateDb() {
    try {
      dropTables();
      createTables();
    } catch ( Exception e ) {
      throw new RuntimeException( "Could not recreate db/tables due to: " + e.getMessage(), e );
    }
  }

  private void dropTables() throws SQLException, ClassNotFoundException {
    Class.forName( configuration.getDriverClassName() );
    Connection connection = null;
    try {
      connection = DriverManager.getConnection( configuration.getUrl(), configuration.getUser(), configuration.getPass() );

      for ( String sql : getDrop() ) {
        Statement statement = connection.createStatement();
        //noinspection UnusedCatchParameter,EmptyCatchBlock
        try {
          statement.execute( sql );
        } catch ( SQLException e ) {
        }
        statement.close();
      }
    } finally {
      if ( connection != null ) {
        connection.close();
      }
    }
  }

  public String[] getDrop() {
    return annotationConfiguration.generateDropSchemaScript( new MySQLDialect() );
  }

  private void createTables() throws ClassNotFoundException, SQLException {
    Class.forName( configuration.getDriverClassName() );
    Connection connection = null;
    try {
      connection = DriverManager.getConnection( configuration.getUrl(), configuration.getUser(), configuration.getPass() );

      for ( String sql : getCreation() ) {
        Statement statement = connection.createStatement();

        //noinspection JDBCExecuteWithNonConstantString
        statement.execute( sql );
        statement.close();
      }
    } finally {
      if ( connection != null ) {
        connection.close();
      }
    }
  }

  public String[] getCreation() {
    try {
      return annotationConfiguration.generateSchemaCreationScript( getDialect( configuration ).newInstance() );
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public boolean verifyConnection() throws SQLException {
    Connection connection = null;

    try {
      Class.forName( configuration.getDriverClassName() );
      connection = DriverManager.getConnection( configuration.getUrl(), configuration.getUser(), configuration.getPass() );
      return true;
    } catch ( Exception e ) {
      e.printStackTrace();
      return false;
    } finally {
      if ( connection != null ) {
        connection.close();
      }
    }
  }


}

