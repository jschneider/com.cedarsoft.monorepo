package com.cedarsoft.gdao;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.testng.Assert.*;

import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * Tests persistence objects
 */
public class PersistenceTester {
  @NotNull
  private final Set<Class<?>> transientFieldTypes = new HashSet<Class<?>>( Arrays.<Class<?>>asList( PropertyChangeSupport.class ) );
  @NotNull
  private final GenericDaoManager daoManager;

  public int additionalObjectCount = 0;
  public boolean checkEquality = false;

  public PersistenceTester( @NotNull GenericDaoManager daoManager ) {
    this.daoManager = daoManager;
  }

  public PersistenceTester( @NotNull GenericDaoManager daoManager, @NotNull Set<Class<?>> transientFieldTypes ) {
    this.daoManager = daoManager;
    this.transientFieldTypes.addAll( transientFieldTypes );
  }

  public <T> void save( @NotNull T object ) {
    GenericDao<T> dao = getDao( object );
    dao.save( object );
  }

  public void addTransientFieldType( @NotNull Class<?> transientFieldType ) {
    transientFieldTypes.add( transientFieldType );
  }

  @NotNull
  public <T> GenericDao<T> getDao( @NotNull T object ) {
    return daoManager.getDao( ( Class<T> ) object.getClass() );
  }

  /**
   * Tests the given object
   *
   * @param object the object that is tested
   */
  public <T> void testObject( @NotNull T object ) throws Exception {
    //Get the DAO
    GenericDao<T> dao = getDao( object );
    assertEquals( additionalObjectCount, dao.findAll().size() );

    //Save the object
    Long id = dao.save( object );
    assertEquals( 1 + additionalObjectCount, dao.findAll().size() );

    //Load it
    T loaded = dao.findById( id );
    compare( object, loaded );

    //Now update
    dao.update( loaded );
    compare( object, dao.findById( id ) );
    assertEquals( 1 + additionalObjectCount, dao.findAll().size() );

    //Now delete
    dao.delete( loaded );
    assertEquals( additionalObjectCount, dao.findAll().size() );

    //check serialization
    if ( object instanceof Serializable ) {
      checkSerialization( object, loaded );
    }
  }

  private <T> void checkSerialization( @NotNull T object, @NotNull T loaded ) throws Exception {
    compare( object, loaded );

    //Ensure transient fields
    for ( Field field : loaded.getClass().getDeclaredFields() ) {
      //Ensure that the transient fields
      if ( mustBeTransient( field ) ) {
        if ( !Modifier.isTransient( field.getModifiers() ) ) {
          throw new IllegalStateException( field.getName() + " must be transient" );
        }
      }
    }

    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ObjectOutput out = new ObjectOutputStream( bytes );
    out.writeObject( loaded );
    out.writeObject( object );

    ObjectInputStream objectInput = new ObjectInputStream( new ByteArrayInputStream( bytes.toByteArray() ) );
    Object loadedRead = objectInput.readObject();
    assertNotNull( loadedRead );
    compare( object, loadedRead );
    compare( loaded, loadedRead );

    Object objectRead = objectInput.readObject();
    compare( object, objectRead );
    compare( loaded, objectRead );
  }

  protected boolean mustBeTransient( @NotNull Field field ) {
    return transientFieldTypes.contains( field.getType() );
  }

  private <T> void compare( @NotNull T object, @NotNull T loaded ) throws Exception {
    assertNotNull( loaded );

    //the fields of the object
    compareFields( object, loaded, loaded.getClass().getDeclaredFields() );

    for ( Class<?> currentClass = loaded.getClass(); currentClass != null; currentClass = currentClass.getSuperclass() ) {
      compareFields( object, loaded, currentClass.getDeclaredFields() );
    }


    assertNotSame( object, loaded );

    if ( checkEquality ) {
      assertEquals( object, loaded );
      assertEquals( object.hashCode(), loaded.hashCode() );

      //Now check whether the Id is relevant
      eliminateFieldRelevance( "id", object, loaded, 99999999L );

      //Now check whether Property Change Support is relevant
      eliminateFieldRelevance( "pcs", object, loaded, new PropertyChangeSupport( object ) );
    }
  }

  private <T> void compareFields( T object, T loaded, Field[] fields ) throws IllegalAccessException {
    for ( Field field : fields ) {
      field.setAccessible( true );

      //Skip transient values
      if ( Modifier.isTransient( field.getModifiers() ) ) {
        continue;
      }

      //Ensure that the PCS is transient
      if ( field.getType().equals( PropertyChangeSupport.class ) ) {
        continue;
      }

      //If it is a lock --> continue
      if ( field.getType().equals( ReadWriteLock.class ) ) {
        continue;
      }
      if ( field.getType().equals( Lock.class ) ) {
        continue;
      }

      Object loadedValue = field.get( loaded );
      Object originalValue = field.get( object );

      if ( originalValue != null ) {
        assertNotNull( loadedValue, "Expected " + originalValue + " but was null for field " + object.getClass().getName() + "#" + field.getName() );
        assertEquals( originalValue.getClass(), loadedValue.getClass() );

        //If it is a collection, check the sizes
        if ( originalValue instanceof Collection ) {
          int originalSize = ( ( Collection<?> ) originalValue ).size();
          int loadedSize = ( ( Collection<?> ) loadedValue ).size();
          assertEquals( originalSize, loadedSize, object.getClass().getName() + "#" + field.getName() + ": Expected <" + originalSize + "> but was <" + loadedSize + ">" );
        }
      }

      if ( checkEquality ) {
        assertEquals( originalValue, loadedValue, "Field value not equal for \"" + field.getDeclaringClass().getName() + "." + field.getName() + "\"" );
      }
    }
  }

  private static <T> void eliminateFieldRelevance( @NonNls @NotNull String fieldName, @NotNull T object, @NotNull T loaded, @Nullable Object newValue ) throws IllegalAccessException {
    //First verify that the objects are equal
    assertEquals( object, loaded );
    assertEquals( object.hashCode(), loaded.hashCode() );

    Field field = null;
    Class<?> currentClass = loaded.getClass();
    while ( field == null && currentClass != null ) {
      try {
        field = currentClass.getDeclaredField( fieldName );
      } catch ( NoSuchFieldException ignore ) {
        currentClass = currentClass.getSuperclass();
      }
    }

    if ( field == null ) {
      return;//This is ok - there does not exist any field with the given name
    }

    field.setAccessible( true );
    Object oldValue = field.get( loaded );
    field.set( loaded, newValue );

    assertEquals( object, loaded, "Field must not be relevant for equals: " + currentClass.getName() + "." + fieldName + " - " );
    assertEquals( object.hashCode(), loaded.hashCode(), "Field must not be relevant for hashCode: " + currentClass.getName() + "." + fieldName + " - " );
    field.set( loaded, oldValue );
  }
}
