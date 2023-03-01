package it.neckar.open.test.utils

import it.neckar.open.test.utils.JsonUtils.assertJsonEquals
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.ComparisonFailure
import org.junit.jupiter.api.Test

/**
 */
class JsonUtilsTest {
  @Test
  fun testIt() {
    try {
      assertJsonEquals("[]", "[\"asdf\"]")
      AssertionsForClassTypes.fail("Where is the Exception")
    } catch (e: ComparisonFailure) {
      AssertionsForClassTypes.assertThat(e).hasMessage("JSON comparison failed expected:<[ []]> but was:<[ [\"asdf\" ]]>")
    }
  }
}
