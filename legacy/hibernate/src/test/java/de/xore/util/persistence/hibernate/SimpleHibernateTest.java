package de.xore.util.persistence.hibernate;

import de.xore.util.persistence.DatabaseConfiguration;
import de.xore.util.persistence.DatabaseType;
import junit.framework.TestCase;
import org.hibernate.Session;

/**
 * <p/>
 * Date: 21.06.2006<br>
 * Time: 22:07:43<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class SimpleHibernateTest extends TestCase {
  public void testDummy() {

  }

  public void _testDBConnector() {
    DatabaseConfiguration configuration = new DatabaseConfiguration( "url", "user", "pass", DatabaseType.MYSQL );
    HibernateDatabaseConnector connector = new HibernateDatabaseConnector( configuration );
    assertNotNull( connector );

    connector.dispose();
  }

  public void _testHibernateQueryManager() {
    DatabaseConfiguration configuration = new DatabaseConfiguration( "url", "user", "pass", DatabaseType.MYSQL );
    HibernateQueryManager queryManager = new HibernateQueryManager( new HibernateDatabaseConnector( configuration ) );
    assertNotNull( queryManager );

    assertFalse( queryManager.getConnector().isSessionActive() );
    queryManager.getConnector().closeSession();

    queryManager.dispose();
  }

  public void _testConnectToDB() {
    DatabaseConfiguration configuration = DatabaseConfiguration.createConfiguration( "hibernate" );
    assertNotNull( configuration );
    assertEquals( DatabaseType.MYSQL, configuration.getDatabaseType() );
    HibernateDatabaseConnector databaseConnector = new HibernateDatabaseConnector( configuration );

    Session session = databaseConnector.openSession();
    assertNotNull( session );
    databaseConnector.closeSession();

    databaseConnector.recreateDb();
    databaseConnector.closeSession();
    databaseConnector.dispose();
  }

  public void _testAnnotatedClass() {
    DatabaseConfiguration configuration = DatabaseConfiguration.createConfiguration( "hibernate" );
    HibernateDatabaseConnector databaseConnector = new HibernateDatabaseConnector( configuration );

    databaseConnector.addAnnotadedClass( UserMock.class );
    databaseConnector.recreateDb();

    UserMock userMock = new UserMock();
    databaseConnector.beginTransaction();
    databaseConnector.save( userMock );
    databaseConnector.commit();
    String id = userMock.getId();
    assertTrue( id != null && id.length() > 0 );

    assertNotNull( databaseConnector.load( UserMock.class, id ) );

    databaseConnector.dispose();
  }
}
