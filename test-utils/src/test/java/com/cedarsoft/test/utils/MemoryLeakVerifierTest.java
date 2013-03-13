package com.cedarsoft.test.utils;

import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;


/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MemoryLeakVerifierTest {
  @Test
  public void testIt() throws Exception {
    List<Object> objects = new ArrayList<Object>();
    objects.add( new Object() );

    MemoryLeakVerifier<Object> leakVerifier = new MemoryLeakVerifier<Object>( objects.get( 0 ) );
    assertThat( leakVerifier.getObject() ).isNotNull();

    objects.clear();
    leakVerifier.assertGarbageCollected();
  }
}
