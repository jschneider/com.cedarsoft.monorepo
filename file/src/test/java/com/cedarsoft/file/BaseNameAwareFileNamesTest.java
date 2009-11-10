package com.cedarsoft.file;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class BaseNameAwareFileNamesTest {
  private BaseNameAwareFileNames report;

  @Test
  public void testDuplicate() {
    report.add( new FileName( "base1", ".", "txt" ) );
    try {
      report.add( new FileName( "base1", ".", "txt" ) );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testIt() {
    BaseNameAwareFileNames report = new BaseNameAwareFileNames();

    report.add( new FileName( "base1", ".", "txt" ) );
    report.add( new FileName( "base1", ".", "txt2" ) );
    report.add( new FileName( "base1", ".", "txt3" ) );

    report.add( new FileName( "base2", ".", "txt" ) );
    report.add( new FileName( "base3", ".", "txt" ) );

    FileNames sameBaseNameEntry = report.getEntry( new BaseName( "base1" ) );
    assertNotNull( sameBaseNameEntry );

    assertEquals( sameBaseNameEntry.getFileNames().size(), 3 );
  }

  @BeforeMethod
  protected void setUp() throws Exception {
    report = new BaseNameAwareFileNames();
  }
}
