/*
 * Copyright (c) 2005 Xore Systems. All Rights Reserved.
 */
package de.xore.util.persistence;

import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.ResourceBundle;

/**
 * Configuration for a specific database
 * <p/>
 * Date: 18.05.2005<br>
 * Time: 23:43:33<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class DatabaseConfiguration {
  public static DatabaseConfiguration createConfiguration( @NotNull String bundlename ) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle( bundlename );
    if ( resourceBundle == null ) {
      throw new DatabaseException( "ResourceBundle not found for bundlename " + bundlename );
    }
    String url = resourceBundle.getString( "url" );
    if ( url == null || url.length() == 0 ) {
      throw new DatabaseException( "invalid url " + url );
    }
    String user = resourceBundle.getString( "user" );
    if ( user == null || user.length() == 0 ) {
      throw new DatabaseException( "invalid user " + user );
    }
    String pass = resourceBundle.getString( "password" );
    if ( pass == null || pass.length() == 0 ) {
      throw new DatabaseException( "invalid password " + pass );
    }
    String databaseTypeAsString = resourceBundle.getString( "type" );
    if ( databaseTypeAsString == null || databaseTypeAsString.length() == 0 ) {
      throw new DatabaseException( "invalid type " + databaseTypeAsString );
    }
    DatabaseType databaseType = DatabaseType.valueOf( databaseTypeAsString );
    return new DatabaseConfiguration( url, user, pass, databaseType );
  }

  @NotNull
  private DatabaseType databaseTypeType = DatabaseType.MYSQL;
  @NotNull
  private String url = "";
  @NotNull
  private String user = "";
  @NotNull
  private String pass = "";

  public DatabaseConfiguration( @NotNull String url, @NotNull String user, @NotNull String pass, @NotNull DatabaseType databaseTypeType ) {
    this.url = url;
    this.user = user;
    this.pass = pass;
    this.databaseTypeType = databaseTypeType;
  }

  @NotNull
  public DatabaseType getDatabaseType() {
    return databaseTypeType;
  }

  @NotNull
  public String getDriverClassName() {
    return databaseTypeType.getDriverClassName();
  }

  public boolean isValid() {
    return databaseTypeType != null && url != null && user != null && pass != null;
  }

  public void setDatabaseType( @NotNull DatabaseType databaseTypeType ) {
    this.databaseTypeType = databaseTypeType;
  }

  @NotNull
  public String getPass() {
    return pass;
  }

  public void setPass( @NotNull String pass ) {
    this.pass = pass;
  }

  @NotNull
  public String getUrl() {
    return url;
  }

  public void setUrl( @NotNull String url ) {
    this.url = url;
  }

  @NotNull
  public String getUser() {
    return user;
  }

  public void setUser( @NotNull String user ) {
    this.user = user;
  }

  @Override
  @NotNull
  @Override
  public String toString() {
    return "DatabaseConfiguration{" +
        "databaseTypeType=" + databaseTypeType +
        ", url='" + url + '\'' +
        ", user='" + user + '\'' +
        '}';
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    final DatabaseConfiguration databaseConfiguration = ( DatabaseConfiguration ) o;

    if ( databaseTypeType != databaseConfiguration.databaseTypeType ) return false;
    if ( pass != null ? !pass.equals( databaseConfiguration.pass ) : databaseConfiguration.pass != null ) return false;
    if ( url != null ? !url.equals( databaseConfiguration.url ) : databaseConfiguration.url != null ) return false;
    if ( user != null ? !user.equals( databaseConfiguration.user ) : databaseConfiguration.user != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = ( databaseTypeType != null ? databaseTypeType.hashCode() : 0 );
    result = 29 * result + ( url != null ? url.hashCode() : 0 );
    result = 29 * result + ( user != null ? user.hashCode() : 0 );
    result = 29 * result + ( pass != null ? pass.hashCode() : 0 );
    return result;
  }
}

