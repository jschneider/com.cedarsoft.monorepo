package com.cedarsoft;

import static org.testng.Assert.*;

import com.cedarsoft.Strings;
import org.testng.annotations.*;

/**
 * <p/>
 * Date: May 29, 2007<br>
 * Time: 2:12:13 PM<br>
 */
public class StringToolsTest {
  @Test
  public void testMaxLengt() {
    assertEquals( "", Strings.cut( "asdf", 0 ) );
    assertEquals( "a", Strings.cut( "asdf", 1 ) );
    assertEquals( "as", Strings.cut( "asdf", 2 ) );
    assertEquals( "asd", Strings.cut( "asdf", 3 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 4 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 5 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 995 ) );
  }

  @Test
  public void testQuote() {
    assertEquals( "", Strings.stripQuotes( "" ) );
    assertEquals( "a", Strings.stripQuotes( "a" ) );
    assertEquals( "a", Strings.stripQuotes( "\"a" ) );
    assertEquals( "a", Strings.stripQuotes( "\"a\"" ) );
    assertEquals( "a", Strings.stripQuotes( "a\"" ) );
  }
}
