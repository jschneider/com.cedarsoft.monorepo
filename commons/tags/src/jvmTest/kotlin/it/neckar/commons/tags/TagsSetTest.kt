package it.neckar.commons.tags

import com.cedarsoft.commons.serialization.roundTrip
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

/**
 *
 */
class TagsSetTest {
  @Test
  fun testSerialization() {
    roundTrip(Tags(Tag("foo"), Tag("bar"))) {
      //language=JSON
      """
          [ "foo", "bar" ]
      """.trimIndent()
    }
  }

  @Test
  fun testSpecialChars() {
    roundTrip(Tags(Tag("foo,{}"), Tag("bar\"&"))) {
      //language=JSON
      """
          [ "foo,{}", "bar\"&" ]
      """.trimIndent()
    }
  }

  @Test
  fun testUsage() {
    @Serializable
    data class MyClass(val theTags: Tags) {
    }

    roundTrip(MyClass(Tags(Tag("foo"), Tag("bar")))) {
      //language=JSON
      """
        {
          "theTags" :  [ "foo", "bar" ]
        }""".trimIndent()
    }
  }
}
