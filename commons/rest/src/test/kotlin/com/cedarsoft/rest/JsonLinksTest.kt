package com.cedarsoft.rest

import assertk.*
import assertk.assertions.*
import com.cedarsoft.test.utils.JsonUtils
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.junit.jupiter.api.Test

/**
 */
internal class JsonLinksTest {
  @Test
  fun testJackson() {
    val objectMapper = JacksonConfiguration.createObjectMapper()

    val json = objectMapper.writeValueAsString(JsonLinks("own", "asdfasfd/asdf", "other", "https://cedarsoft.com"))

    JsonUtils.assertJsonEquals(
      "{\n" +
        "  \"own\" : \"asdfasfd/asdf\",\n" +
        "  \"other\" : \"https://cedarsoft.com\"\n" +
        "}", json
    )
  }

  @Test
  internal fun testWithLinksApi() {
    val data = MyObject("Markus Mustermann", 13)

    data.withLink(JsonLink("home", "https://cedarsoft.com"))
    val withLinks = data.withLinks(JsonLink("home", "https://cedarsoft.com"), JsonLink("home2", "https://cedarsoft.com2"))

    assertThat(withLinks.links.links).hasSize(2)
  }

  @Test
  internal fun testWithLinks() {
    val data = MyObject("Markus Mustermann", 13)
    val withLinks = data.withLinks(JsonLinks("home", "https://cedarsoft.com"))

    val objectMapper = JacksonConfiguration.createObjectMapper()


    val json = objectMapper.writeValueAsString(withLinks)

    JsonUtils.assertJsonEquals(
      "{\n" +
        "  \"name\" : \"Markus Mustermann\",\n" +
        "  \"age\" : 13,\n" +
        "  \"links\" : {\n" +
        "    \"home\" : \"https://cedarsoft.com\"\n" +
        "  }\n" +
        "}", json
    )
  }

  @Test
  internal fun testParse() {
    val data = MyObject("Markus Mustermann", 13)
    val withLinks = data.withLinks(JsonLinks("home", "https://cedarsoft.com"))
    val objectMapper = JacksonConfiguration.createObjectMapper()

    val json = objectMapper.writeValueAsString(withLinks)

    val read = objectMapper.readValue(json, MyObject::class.java)
    assertThat(read.name).isEqualTo("Markus Mustermann")
    assertThat(read.age).isEqualTo(13)
  }
}

@JsonIgnoreProperties(com.cedarsoft.rest.WithLinks.LINKS_PROPERTY)
data class MyObject(
  val name: String,
  val age: Int
)
