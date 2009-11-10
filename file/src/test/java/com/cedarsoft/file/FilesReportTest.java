package com.cedarsoft.file;

import com.cedarsoft.FileName;
import com.cedarsoft.file.FilesReport;
import com.cedarsoft.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.*;

/**
 *
 */
public class FilesReportTest {
  private FilesReport report;
  private Provider<InputStream, IOException> provider;

  @Test
  public void testDuplicate() {
    report.add( new FileName( "base1", ".", "txt" ), provider );
    try {
      report.add( new FileName( "base1", ".", "txt" ),provider );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testIt() {
    FilesReport report = new FilesReport();

    report.add( new FileName( "base1", ".", "txt" ) ,provider);
    report.add( new FileName( "base1", ".", "txt2" ) ,provider);
    report.add( new FileName( "base1", ".", "txt3" ) ,provider);

    report.add( new FileName( "base2", ".", "txt" ) ,provider);
    report.add( new FileName( "base3", ".", "txt" ) ,provider);

    FilesReport.BaseNameEntry baseNameEntry = report.getEntry( "base1" );
    assertNotNull( baseNameEntry );

    assertEquals( baseNameEntry.getEntries().size(), 3 );
  }

  @BeforeMethod
  protected void setUp() throws Exception {
    report = new FilesReport();
    provider = new Provider<InputStream,IOException>() {
      @NotNull
      @Override
      public InputStream provide() {
        throw new UnsupportedOperationException();
      }

      @NotNull
      @Override
      public String getDescription() {
        return "asdf";
      }
    };
  }
}
