/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.tags;

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
