package com.cedarsoft.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule

object JacksonConfiguration {
  /**
   * Creates a new object mapper with the correct configuration
   */
  @JvmStatic
  fun createObjectMapper(): ObjectMapper {
    return jacksonObjectMapper()
      .registerModule(ParameterNamesModule())
      .registerModule(Jdk8Module())
      .registerModule(JavaTimeModule())
      .registerModule(GuavaModule())
  }

}
