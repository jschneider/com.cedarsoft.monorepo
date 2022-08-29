package com.cedarsoft.rest

import com.fasterxml.jackson.annotation.JsonUnwrapped

/**
 * Wrapper class that contains a data object and links
 */
data class WithLinks<T>(
  @JsonUnwrapped
  val data: T,
  val links: JsonLinks
) {
  companion object {
    /**
     * The property name of the links property.
     * Is used for JACKSON serialization
     */
    const val LINKS_PROPERTY = "links"
  }
}

/**
 * Creates a wrapper object that contains the given links
 */
fun <T> T.withLinks(links: JsonLinks): WithLinks<T> {
  return WithLinks(this, links)
}

fun <T> T.withLinks(vararg links: JsonLink): WithLinks<T> {
  return WithLinks(this, JsonLinks(*links))
}

fun <T> T.withLink(link: JsonLink): WithLinks<T> {
  return WithLinks(this, JsonLinks(link))
}
