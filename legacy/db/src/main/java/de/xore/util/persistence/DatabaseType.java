/*
 * Copyright (c) 2005 Xore Systems. All Rights Reserved.
 */
package de.xore.util.persistence;

/**
 * <p> Date: 19.04.2005<br> Time: 20:15:45<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public enum DatabaseType {
  MYSQL( "com.mysql.jdbc.Driver" ),
  FIREBIRD( "org.firebirdsql.jdbc.FBDriver" );

  private String driverClassName;

  DatabaseType( String driverClassName ) {
    this.driverClassName = driverClassName;
  }

  public boolean isDriverClassAvailable() {
    try {
      Class.forName( driverClassName );
      return true;
    } catch ( ClassNotFoundException ignore ) {
      return false;
    }
  }

  public String getDriverClassName() {
    return driverClassName;
  }
}
