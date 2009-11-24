package com.cedarsoft.cmd;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p/>
 * Date: Mar 7, 2007<br>
 * Time: 10:42:16 AM<br>
 */
public class DefaultCmdLineTest {
  private StringCmdLine cmdLine;

  @BeforeMethod
  protected void setUp() throws Exception {
    cmdLine = new StringCmdLine();
  }

  @Test
  public void testReadWIthSelectionAndPreselection() {
    cmdLine.addExpectedOut( "asdf" );
    cmdLine.addExpectedOut( "\t0\t-->0" );
    cmdLine.addExpectedOut( "\t1\t-->1" );

    cmdLine.addAnswer( "" );

    List<String> values = new ArrayList<String>();
    values.add( "-->0" );
    values.add( "-->1" );
    String value = cmdLine.read( "asdf", values, "other" );

    assertEquals( "other", value );
  }

  @Test
  public void testListOwn() {
    cmdLine.addAnswer( "d" );
    cmdLine.addExpectedOut( "message" );
    cmdLine.addExpectedOut( "\t0\ta" );
    cmdLine.addExpectedOut( "\t1\tb" );
    cmdLine.addExpectedOut( "\t2\tc" );

    assertEquals( "d", cmdLine.read( "message", Arrays.asList( "a", "b", "c" ) ) );
  }

  @Test
  public void testReadWithSelection() throws IOException {
    cmdLine.addExpectedOut( "asdf" );
    cmdLine.addExpectedOut( "\t0\t-->0" );
    cmdLine.addExpectedOut( "\t1\t-->1" );

    cmdLine.addAnswer( "daFreeValue" );

    List<String> values = new ArrayList<String>();
    values.add( "-->0" );
    values.add( "-->1" );
    String value = cmdLine.read( "asdf", values );

    assertEquals( "daFreeValue", value );
  }

  @Test
  public void testReadWithSelection2() throws IOException {
    cmdLine.addExpectedOut( "asdf" );
    cmdLine.addExpectedOut( "\t0\t-->0" );
    cmdLine.addExpectedOut( "\t1\t-->1" );

    cmdLine.addAnswer( "0" );

    List<String> values = new ArrayList<String>();
    values.add( "-->0" );
    values.add( "-->1" );

    assertEquals( "-->0", cmdLine.read( "asdf", values ) );
  }
}
