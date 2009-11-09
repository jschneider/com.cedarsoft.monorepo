package com.cedarsoft.gdao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.Override;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * User type that is able to
 */
public class EnumUserType<E extends Enum<E>> implements UserType, ParameterizedType {
  @NotNull
  private static final int[] SQL_TYPES = {Types.VARCHAR};

  @NotNull
  private Class<E> enumType;
  @NonNls
  public static final String ENUM_CLASS_NAME = "enumClassName";

  @Override
  public void setParameterValues( Properties parameters ) {
    if ( parameters == null ) {
      throw new MappingException( "Must set enumClassName as parameter" );
    }

    String enumClassName = parameters.getProperty( ENUM_CLASS_NAME );
    if ( enumClassName == null ) {
      throw new MappingException( "enumClassName parameter not specified" );
    }

    try {
      this.enumType = ( Class<E> ) Class.forName( enumClassName );
    } catch ( ClassNotFoundException e ) {
      throw new MappingException( "enumClass " + enumClassName + " not found", e );
    }
  }

  @Override
  public int[] sqlTypes() {
    //noinspection ReturnOfCollectionOrArrayField
    return SQL_TYPES;
  }

  @Override
  public Class<?> returnedClass() {
    return enumType;
  }

  @Override
  public Object nullSafeGet( ResultSet rs, String[] names, Object owner ) throws HibernateException, SQLException {
    String name = rs.getString( names[0] );
    E result = null;
    if ( !rs.wasNull() ) {
      result = Enum.valueOf( enumType, name );
    }
    return result;
  }

  @Override
  public void nullSafeSet( PreparedStatement st, Object value, int index ) throws HibernateException, SQLException {
    if ( null == value ) {
      st.setNull( index, Types.VARCHAR );
    } else {
      st.setString( index, ( ( Enum<?> ) value ).name() );
    }
  }

  @Override
  public Object deepCopy( Object value ) throws HibernateException {
    return value;
  }

  @Override
  public boolean isMutable() {
    return false;
  }

  @Override
  public Object assemble( Serializable cached, Object owner ) throws HibernateException {
    return cached;
  }

  @Override
  public Serializable disassemble( Object value ) throws HibernateException {
    return ( Serializable ) value;
  }

  @Override
  public Object replace( Object original, Object target, Object owner ) throws HibernateException {
    return original;
  }

  @Override
  public int hashCode( Object x ) throws HibernateException {
    return x.hashCode();
  }

  @Override
  public boolean equals( Object x, Object y ) throws HibernateException {
    if ( x == y )
      return true;
    if ( null == x || null == y ) {
      return false;
    }
    return x.equals( y );
  }
}