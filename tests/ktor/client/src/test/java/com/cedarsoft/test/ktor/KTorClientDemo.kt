package com.cedarsoft.test.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.call.typeInfo
import io.ktor.client.engine.apache.Apache
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.Fail
import org.junit.jupiter.api.Test

/**
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
class KTorClientDemo {
  @Test
  internal fun testFailing() {
    Fail.fail<Unit>("uups")
  }

  @Test
  internal fun testClient() {
    runBlocking {
      println("asdf")
      val client = HttpClient(Apache)

      val call = client.call("http://localhost:8080")
      assertThat(call.receive(typeInfo<String>())).isEqualTo("Hello World")
      call.close()

      client.close()
      Unit
    }
  }
}
