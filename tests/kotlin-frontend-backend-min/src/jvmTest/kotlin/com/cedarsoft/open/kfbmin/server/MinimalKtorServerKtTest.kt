package com.cedarsoft.open.kfbmin.server

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.support.expected
import assertk.assertions.support.show
import com.cedarsoft.open.kfbmin.HelloWorldData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@UnstableDefault
@ExperimentalCoroutinesApi
internal class MinimalKtorServerKtTest {
  lateinit var json: Json

  @BeforeEach
  internal fun setUp() {
    val server = MinimalKtorServer()
    json = server.json
  }

  @Test
  fun testGet() {
    withTestApplication({ launchServer() }) {
      runBlocking {
        handleRequest(HttpMethod.Get, "hello-world") {
        }.apply {
          Assertions.assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
          Assertions.assertThat(response.contentType().withoutParameters()).isEqualTo(ContentType.Application.Json)
          assertThat(response.content).isNotNull()
            .transform {
              json.parse(HelloWorldData.serializer(), it)
            }.all {
              hasMessage("Hello World")
            }
        }
      }
    }
  }

  @Test
  fun testPut() {
    withTestApplication({ launchServer() }) {
      runBlocking {
        handleRequest(HttpMethod.Put, "hello-world") {
          setBody(json.stringify(HelloWorldData.serializer(), HelloWorldData("My message")))
        }.apply {
          Assertions.assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
        }

        //Check that the get returns the updated value
        handleRequest(HttpMethod.Get, "hello-world") {
        }.apply {
          Assertions.assertThat(response.status()).isEqualTo(HttpStatusCode.OK)
          Assertions.assertThat(response.contentType().withoutParameters()).isEqualTo(ContentType.Application.Json)
          assertThat(response.content).isNotNull()
            .transform {
              json.parse(HelloWorldData.serializer(), it)
            }.all {
              hasMessage("My message")
            }
        }

      }
    }
  }
}


fun Assert<HelloWorldData>.hasMessage(expected: String) = given { actual ->
  if (actual.message == expected) {
    return@given
  }

  expected("message:${show(expected)} but was message:${show(actual.message)}")
}
