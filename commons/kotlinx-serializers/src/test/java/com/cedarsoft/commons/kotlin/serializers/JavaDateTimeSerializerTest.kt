package com.cedarsoft.commons.kotlin.serializers

import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.*
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
    val json = Json.stringify(LocalDateSerializer, obj)
    println("json: $json")
    val parsed = Json.parse(LocalDateSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testLocalTime() {
    val obj = LocalTime.now()
    val json = Json.stringify(LocalTimeSerializer, obj)
    println("json: $json")
    val parsed = Json.parse(LocalTimeSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testDuration() {
    val obj = Duration.ofMinutes(77)
    val json = Json.stringify(DurationSerializer, obj)
    println("json: $json")
    val parsed = Json.parse(DurationSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testPeriod() {
    val obj = Period.ofDays(44)
    val json = Json.stringify(PeriodSerializer, obj)
    println("json: $json")
    val parsed = Json.parse(PeriodSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }
}