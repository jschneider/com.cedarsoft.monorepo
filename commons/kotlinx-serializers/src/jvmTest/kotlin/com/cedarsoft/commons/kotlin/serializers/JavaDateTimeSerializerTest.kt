package com.cedarsoft.commons.kotlin.serializers

import assertk.*
import assertk.assertions.*
import com.cedarsoft.test.utils.JsonUtils
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

/**
 */
internal class JavaDateTimeSerializerTest {
  @Test
  internal fun testLocalDate() {
    val obj = LocalDate.of(2021, 5, 4)
    val json = Json.encodeToString(LocalDateSerializer, obj)

    JsonUtils.assertJsonEquals("\"2021-05-04\"", json)

    val parsed = Json.decodeFromString(LocalDateSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testLocalTime() {
    val obj = LocalTime.of(14, 43, 5, 709_000_000)
    val json = Json.encodeToString(LocalTimeSerializer, obj)

    JsonUtils.assertJsonEquals("\"14:43:05.709\"", json)

    val parsed = Json.decodeFromString(LocalTimeSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testDuration() {
    val obj = Duration.ofMinutes(77)
    val json = Json.encodeToString(DurationSerializer, obj)

    JsonUtils.assertJsonEquals("\"PT1H17M\"", json)

    val parsed = Json.decodeFromString(DurationSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }

  @Test
  internal fun testPeriod() {
    val obj = Period.ofDays(44)
    val json = Json.encodeToString(PeriodSerializer, obj)

    JsonUtils.assertJsonEquals("\"P44D\"", json)

    val parsed = Json.decodeFromString(PeriodSerializer, json)
    assertThat(parsed).isEqualTo(obj)
  }
}
