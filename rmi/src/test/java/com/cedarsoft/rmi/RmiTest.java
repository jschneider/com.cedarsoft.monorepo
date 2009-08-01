package com.cedarsoft.rmi;

import com.cedarsoft.gdao.GenericDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class RmiTest {
  private ClassPathXmlApplicationContext context;
  private GenericDao<Address> dao;
  private ClassPathXmlApplicationContext server;
  private static final int COUNT = 5000;

  @BeforeMethod
  protected void setUp() throws Exception {
    server = new ClassPathXmlApplicationContext( new String[]{"rmi.server.spr.xml", "hibernate.spr.xml"}, RmiTest.class );
    assertNotNull( this.server.getBean( "addressDao" ) );

    context = new ClassPathXmlApplicationContext( "rmi.spr.xml", RmiTest.class );
    assertNotNull( context );
    assertNotNull( context.getBean( "addressDao" ) );

    dao = ( GenericDao<Address> ) context.getBean( "addressDao" );
  }

  @Test
  public void testBasic() {
    assertNotNull( context );
    assertNotNull( dao );
    assertEquals( 0, dao.findAll().size() );
  }

  @Test
  public void testIt() {
    Address address = new Address( "a", "b" );
    dao.save( address );
    assertEquals( 1, dao.findAll().size() );

    Address loaded = dao.findAll().iterator().next();
    assertEquals( "a", loaded.getStreet1() );
    assertEquals( "b", loaded.getStreet2() );

    assertNotSame( address, loaded );
  }

  @Test
  public void testPerformanceCustomer() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start( "Persist Customer" );

    GenericDao<Customer> dao = ( GenericDao<Customer> ) context.getBean( "customerDao" );
    for ( int i = 0; i < COUNT; i++ ) {
      dao.save( new Customer( new Address( "street1_" + i, "street2_" + i ) ) );
    }
    stopWatch.stop();

    stopWatch.start( "Select All Customers" );
    assertEquals( COUNT, dao.findAll().size() );
    stopWatch.stop();

    System.out.println( stopWatch.prettyPrint() );
  }

  @Test
  public void testPerformance() {
    StopWatch stopWatch = new StopWatch();

    stopWatch.start( "Persit Address" );
    for ( int i = 0; i < COUNT; i++ ) {
      dao.save( new Address( "street1_" + i, "street2_" + i ) );
    }
    stopWatch.stop();

    stopWatch.start( "Size" );
    assertEquals( COUNT, dao.findAll().size() );
    stopWatch.stop();


    stopWatch.start( "Read Addresses" );
    for ( Address address : dao.findAll() ) {
      assertNotNull( address.getStreet1() );
      assertNotNull( address.getStreet2() );
    }

    stopWatch.stop();

    System.out.println( stopWatch.prettyPrint() );
  }

  @Test
  public void testWithoutRmi() throws InterruptedException {
    dao = ( GenericDao<Address> ) server.getBean( "addressDao" );

    StopWatch stopWatch = new StopWatch();

    stopWatch.start( "Direct Persist" );
    for ( int i = 0; i < COUNT; i++ ) {
      dao.save( new Address( "street1_" + i, "street2_" + i ) );
    }
    stopWatch.stop();

    stopWatch.start( "Direct Size" );
    assertEquals( COUNT, dao.findAll().size() );
    stopWatch.stop();

    stopWatch.start( "Direct Read" );
    for ( Address address : dao.findAll() ) {
      assertNotNull( address.getStreet1() );
      assertNotNull( address.getStreet2() );
    }

    stopWatch.stop();

    System.out.println( stopWatch.prettyPrint() );
  }
}
