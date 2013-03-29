package com.cedarsoft.annotations.instrumentation;

import org.junit.*;

import java.text.MessageFormat;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NonNullAnnotationTransformerTest {
  @Test
  public void testMessageFormat() throws Exception {
    MessageFormat.format( NonNullAnnotationTransformer.NON_NULL_PARAM, 1 );
  }
}
