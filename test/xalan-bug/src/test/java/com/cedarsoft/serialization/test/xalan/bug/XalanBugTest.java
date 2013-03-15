package com.cedarsoft.serialization.test.xalan.bug;

import com.cedarsoft.xml.XmlCommons;
import org.junit.*;

import javax.xml.transform.Transformer;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Reproduces https://github.com/jschneider/com.cedarsoft.serialization/issues/2
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class XalanBugTest {
  @Test
  public void testIt() throws Exception {
    Transformer transformer = XmlCommons.createTransformer();
    assertThat( transformer ).isNotNull();
  }
}
