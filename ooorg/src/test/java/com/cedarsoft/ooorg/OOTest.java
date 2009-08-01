package com.cedarsoft.ooorg;

import org.testng.annotations.*;

/**
 *
 */
public class OOTest {
  @BeforeMethod
  protected void setUp() throws Exception {
  }

  @Test
  public void testDummy() {

  }

  //  public void DONOTtestStartOO() throws InterruptedException, IOException {
  //    File source = new File( "/tmp/test.doc" );
  //    File out = File.createTempFile( "test", ".pdf" );
  //
  //    OpenOfficeConnection connection = new SocketOpenOfficeConnection( 8100 );
  //    connection.connect();
  //
  //    try {
  //      DocumentConverter converter = new StreamOpenOfficeDocumentConverter( connection );
  //      converter.convert( source, out );
  //
  //      System.out.println( "Converted successfully: " + out.getAbsolutePath() );
  //    } finally {
  //      connection.disconnect();
  //    }
  //  }
}
