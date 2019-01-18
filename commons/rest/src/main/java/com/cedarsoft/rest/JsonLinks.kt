package com.cedarsoft.rest

import com.cedarsoft.guava.toImmutable
import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonCreator
import com.google.common.collect.ImmutableMap
import kotlin.collections.associateBy

/**
 * Contains some links
 */
data class JsonLinks
@JsonCreator
constructor(
  @get:JsonAnyGetter
  val links: ImmutableMap<String, String>
) {

  constructor(link: JsonLink) : this(link.toMap())

  constructor(vararg links: JsonLink) : this(links.asMap())

  constructor(links: Map<String, String>) : this(ImmutableMap.copyOf(links))

  constructor(type: String, link: String) : this(ImmutableMap.of<String, String>(type, link))

  constructor(type: String, link: String, type2: String, link2: String) : this(ImmutableMap.of<String, String>(type, link, type2, link2))

  constructor(type: String, link: String, type2: String, link2: String, type3: String, link3: String) : this(ImmutableMap.of<String, String>(type, link, type2, link2, type3, link3))

  constructor(type: String, link: String, type2: String, link2: String, type3: String, link3: String, type4: String, link4: String) : this(ImmutableMap.of<String, String>(type, link, type2, link2, type3, link3, type4, link4))
}

private fun Array<out JsonLink>.asMap(): ImmutableMap<String, String> {
  return associateBy({ it.type }, { it.link }).toImmutable()
}

/**
 * Represents one link
 */
data class JsonLink(
  val type: String,
  val link: String
) {

  /**
   * Converts this link to a map
   */
  internal fun toMap(): ImmutableMap<String, String> {
    return ImmutableMap.of(type, link)
  }

  companion object {
    const val SELF = "self"
  }
}
