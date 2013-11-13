package com.cedarsoft.annotations.instrumentation;

import java.io.IOException;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class NonNullGuavaAnnotationTransformer extends DefaultNonNullAnnotationTransformer {
  public NonNullGuavaAnnotationTransformer() throws IOException {
    super( NonNullGuavaAnnotationTransformer.class.getResourceAsStream( "nonNullReturnValueGuava.txt" ), NonNullGuavaAnnotationTransformer.class.getResourceAsStream( "nonNullParamGuava.txt" ) );
  }
}
