package com.cedarsoft.commons.kotlin.serializers

import assertk.*
import assertk.assertions.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
internal class JavaDateTimeSerializerTest {
  @Test
  internal fun testLocalDate() {
    val obj = LocalDate.now()
    val json = Json.encodeToString(LocalDateSerializer, obj)
    println("json: $json")
    val parsed = Json.decodeFromString(LocalDateSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testLocalTime() {
    val obj = LocalTime.now()
    val json = Json.encodeToString(LocalTimeSerializer, obj)
    println("json: $json")
    val parsed = Json.decodeFromString(LocalTimeSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testDuration() {
    val obj = Duration.ofMinutes(77)
    val json = Json.encodeToString(DurationSerializer, obj)
    println("json: $json")
    val parsed = Json.decodeFromString(DurationSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testPeriod() {
    val obj = Period.ofDays(44)
    val json = Json.encodeToString(PeriodSerializer, obj)
    println("json: $json")
    val parsed = Json.decodeFromString(PeriodSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }
}
