package com.cedarsoft.gdao.async;

import com.cedarsoft.async.AsynchroniousInvocationException;
import com.cedarsoft.gdao.AbstractInstanceFinder;
import com.cedarsoft.gdao.MyObject;
import org.hibernate.Criteria;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class AsynchroniousDaoTest extends AsyncTest {
  @Test
  public void testIt() {
    assertEquals( 0, asyncDao.getCount() );
    MyObject object = new MyObject( "saved" );
    asyncDao.save( object );
    assertEquals( 1, asyncDao.getCount() );

    assertEquals( "saved", asyncDao.getElements().get( 0 ).getName() );
    assertNotNull( object.getId() );

    asyncDao.remove( object );
    assertEquals( 0, asyncDao.getCount() );
  }

  @Test
  public void testException() {
    assertEquals( 0, asyncDao.getCount() );
    try {
      asyncDao.findById( 1L );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
      assertEquals( e.getCause().getClass(), EmptyResultDataAccessException.class );
    }
    assertEquals( 0, asyncDao.getCount() );

    try {
      asyncDao.find( new AbstractInstanceFinder<MyObject>( MyObject.class ) {
        protected void addRestrictions( @NotNull Criteria criteria ) {
          throw new IllegalStateException( "Hehe" );
        }
      } );
      fail( "Where is the Exception" );
    } catch ( AsynchroniousInvocationException e ) {
      assertEquals( e.getCause().getClass(), IllegalStateException.class );
    }
  }

  @Test
  public void testMultipleThreads() {
    new Thread( new Runnable() {
      public void run() {
        assertEquals( 0, asyncDao.getCount() );
      }
    } ).start();

    assertEquals( 0, asyncDao.getCount() );

    new Thread( new Runnable() {
      public void run() {
        try {
          Thread.sleep( 1000 );
        } catch ( InterruptedException ignore ) {
        }
        asyncDao.save( new MyObject() );//waits for one second
        assertEquals( 1, asyncDao.getCount() );
      }
    } ).start();

    assertEquals( 0, asyncDao.getCount() );
    assertEquals( 0, asyncDao.getCount() );
    long start = System.currentTimeMillis();
    while ( asyncDao.getCount() == 0 ) {
      asyncDao.getCount();
    }

    long end = System.currentTimeMillis();

    assertTrue( end - start > 990 );
    assertEquals( 1, asyncDao.getCount() );
  }
}
