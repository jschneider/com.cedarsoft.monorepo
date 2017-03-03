package com.cedarsoft.annotations.instrumentation;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NonNullAnnotationTransformer extends DefaultNonNullAnnotationTransformer {
  public NonNullAnnotationTransformer() throws IOException {
    super( NonNullAnnotationTransformer.class.getResourceAsStream( "nonNullReturnValue.txt" ), NonNullAnnotationTransformer.class.getResourceAsStream( "nonNullParam.txt" ) );
  }
}
