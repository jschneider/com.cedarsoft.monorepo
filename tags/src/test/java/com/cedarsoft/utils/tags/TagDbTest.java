package com.cedarsoft.utils.tags;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.dialect.Dialect;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p/>
 * Date: Apr 2, 2007<br>
 * Time: 3:23:09 PM<br>
 */
public abstract class TagDbTest {
  private static final SessionFactory sessionFactory;
  private static final AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration();

  static {
    try {
      annotationConfiguration.configure();
      sessionFactory = annotationConfiguration.buildSessionFactory();
    } catch ( Throwable ex ) {
      // Log exception!
      throw new ExceptionInInitializerError( ex );
    }
  }

  @NotNull
  public static AnnotationConfiguration getAnnotationConfiguration() {
    return annotationConfiguration;
  }

  public static Session getSession() throws HibernateException {
    return sessionFactory.openSession();
  }

  public static void recreateDatabase() throws ClassNotFoundException, SQLException {
    Class.forName( getDriver() );
    Connection connection = DriverManager.getConnection( getUrl(), getUser(), getPassword() );

    Dialect dialect = Dialect.getDialect( annotationConfiguration.getProperties() );

    String[] dropStatements = annotationConfiguration.generateDropSchemaScript( dialect );
    for ( String dropStatement : dropStatements ) {
      try {
        connection.createStatement().execute( dropStatement );
      } catch ( SQLException ignore ) {
      }
    }

    String[] schemaCreations = annotationConfiguration.generateSchemaCreationScript( dialect );
    for ( String schemaCreation : schemaCreations ) {
      try {
        connection.createStatement().execute( schemaCreation );
        System.out.println( "#Creating: " + schemaCreation );
      } catch ( SQLException ignore ) {
      }
    }
    connection.close();
  }

  protected static String getDriver() {
    return annotationConfiguration.getProperty( "hibernate.connection.driver_class" );
  }

  protected static String getUrl() {
    return annotationConfiguration.getProperty( "hibernate.connection.url" );
  }

  protected static String getUser() {
    return annotationConfiguration.getProperty( "hibernate.connection.username" );
  }

  protected static String getPassword() {
    return annotationConfiguration.getProperty( "hibernate.connection.password" );
  }

  public static SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  protected Session session;

  @BeforeMethod
  protected void setUp() throws Exception {
    recreateDatabase();
    session = sessionFactory.openSession();
  }

  protected void reopenSession() {
    session.close();
    session = sessionFactory.openSession();
  }
}
