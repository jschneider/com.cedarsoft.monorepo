package com.cedarsoft.gdao.caching;

import com.cedarsoft.gdao.AbstractDaoTest;
import com.cedarsoft.gdao.GenericDao;
import com.cedarsoft.gdao.MyObject;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 *
 */
public class CachingDaoManagerTest extends AbstractDaoTest {
  private GenericDao<MyObject> cachingDao;

  @Override
  @BeforeMethod
  protected void setUp() throws Exception {
    super.setUp();
    CachingDaoManager cachingDaoManager = new CachingDaoManager( daoManager );
    cachingDao = cachingDaoManager.getDao( MyObject.class );
  }

  @AfterMethod
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void testSaveUpdate() {
    MyObject myObject = new MyObject();
    cachingDao.saveOrUpdate( myObject );
    assertEquals( 1, cachingDao.getCount() );
    cachingDao.saveOrUpdate( myObject );
    assertEquals( 1, cachingDao.getCount() );
    cachingDao.saveOrUpdate( new MyObject() );
    assertEquals( 2, cachingDao.getCount() );
  }
}
