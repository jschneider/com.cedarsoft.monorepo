package com.cedarsoft.test.utils;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.*;
import org.junit.jupiter.api.Test;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class JsonUtilsTest {
  @Test
  void testIt() throws Exception {
    try {
      JsonUtils.assertJsonEquals("[]", "[\"asdf\"]");
      fail("Where is the Exception");
    }
    catch (ComparisonFailure e) {
      assertThat(e).hasMessage("JSON comparison failed expected:<[ []]> but was:<[ [\"asdf\" ]]>");
    }
  }
}
