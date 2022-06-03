package com.cedarsoft.test.utils;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.*;
import org.junit.jupiter.api.Test;

/**
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
