package de.xore.util.html;

import junit.framework.TestCase;

/**
 * <p/>
 * Date: 15.09.2006<br>
 * Time: 22:08:53<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class HtmlEncoderTest extends TestCase {
  public void testUnicode() {
    assertEquals( "&agrave;", HtmlEncoder.escapeHTML( '\u00e0' + "" ) );
    assertEquals( "&euro;", HtmlEncoder.escapeHTML( '\u20ac' + "" ) );
  }

}
