package it.neckar.commons.tags

import assertk.*
import assertk.assertions.*
import com.cedarsoft.commons.serialization.roundTrip
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

/**
 *
 */
class TagsSetTest {
  @Test
  fun testCreate() {
    (Tag("a") + Tag("b")).let {
      assertThat(it.tags).hasSize(2)
      assertThat(it.tags).contains(Tag("a"))
      assertThat(it.tags).contains(Tag("b"))
    }
  }

  @Test
  fun testPlus() {
    val tags = Tags(Tag("a"))

    assertThat(tags.tags).hasSize(1)

    (tags + Tag("b")).let {
      assertThat(it.tags).hasSize(2)
      assertThat(it.tags).contains(Tag("b"))
    }
  }

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
