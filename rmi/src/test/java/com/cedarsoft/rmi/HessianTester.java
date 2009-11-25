package com.cedarsoft.rmi;

import com.cedarsoft.gdao.GenericService;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.testng.annotations.*;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;

import static org.testng.Assert.*;

/**
 *
 */
public class HessianTester {
  private static final int COUNT = 5000;
  private ApplicationContext context;
  private Server server;

  @BeforeMethod
  protected void setUp() throws Exception {
    startServer();

    context = new ClassPathXmlApplicationContext( new String[]{"hessian.client.spr.xml"}, HessianTester.class );
  }

  @AfterMethod
  protected void tearDown() throws Exception {
    server.stop();
  }

  public static void main( String[] args ) throws Exception {
    new HessianTester().startServer();
  }

  private void startServer() throws Exception {
    server = new Server( 8082 );

    Context root = new Context( server, "/", Context.SESSIONS );
    ServletHolder holder = new ServletHolder( new DispatcherServlet() );
    holder.setInitParameter( "contextConfigLocation", "classpath:/com/cedarsoft/rmi/hessian.server.spr.xml" );
    root.addServlet( holder, "/*" );


    ServletContext servletContext = root.getServletContext();
    assertNotNull( servletContext );

    GenericWebApplicationContext webAppContext = new GenericWebApplicationContext();
    webAppContext.setServletContext( servletContext );
    //    webAppContext.setParent( serverContext );
    webAppContext.refresh();

    servletContext.setAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webAppContext );

    server.start();
  }

  @Test
  public void testRound() throws MalformedURLException, ClassNotFoundException {
    GenericService<Customer> service = ( GenericService<Customer> ) context.getBean( "customerService" );
    assertNotNull( service );
    assertEquals( 0, service.getCount() );
  }

  @Test
  public void testPerformanceCustomer() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start( "Persist Customer" );

    GenericService<Customer> dao = ( GenericService<Customer> ) context.getBean( "customerService" );
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
  public void testPerformanceAddress() {
    GenericService<Address> dao = ( GenericService<Address> ) context.getBean( "addressService" );

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
}


