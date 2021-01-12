package com.cedarsoft.commons.ktor

import com.cedarsoft.commons.kotlin.serializers.ImmutableListSerializer
import com.google.common.collect.ImmutableList
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.testing.TestApplicationRequest
import io.ktor.server.testing.TestApplicationResponse
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class AbstractKotlinxSerializationConverterTest {
  abstract val contentType: ContentType
  abstract val contentConverter: KotlinxSerializationConverter

  @Test
  fun testEntity() = withTestApplication {
    application.install(ContentNegotiation) {
      register(contentType, contentConverter.apply {
        register(Entity.serializer())
      })
    }

    val model = Entity(777, "Cargo", listOf(Child("Qube", 1), Child("Sphere", 2), Child("äüö\$", 3)))

    application.routing {
      get("/") {
        call.respond(model)
      }
      post("/") {
        val entity = call.receive<Entity>()
        assertEquals(model, entity)
        call.respond(entity.toString())
      }
    }

    handleRequest(HttpMethod.Get, "/") {
      addHeader("Accept", contentType.toString())
    }.response.let { response ->
      assertEquals(HttpStatusCode.OK, response.status())
      assertNotNull(response.content)
      assertEquals(model, parseEntityResponse(response))
      assertEquals(contentType, response.contentType().withoutParameters())
    }

    handleRequest(HttpMethod.Post, "/") {
      addHeader("Content-Type", contentType.toString())
      createEntityRequest(model, this)
    }.response.let { response ->
      assertEquals(HttpStatusCode.OK, response.status())
      assertNotNull(response.content)
      assertEquals(
        listOf("""Entity(id=777, name=Cargo, children=[Child(item=Qube, quantity=1), Child(item=Sphere, quantity=2), Child(item=äüö${'$'}, quantity=3)])"""),
        response.content!!.lines()
      )
      assertEquals(ContentType.Text.Plain.withCharset(Charsets.UTF_8), response.contentType())
    }
  }

  @Test
  fun testMultiMessage() = withTestApplication {
    application.install(ContentNegotiation) {
      register(contentType, contentConverter.apply {
        register(MultiMessage.serializer())
      })
    }

    val model = MultiMessage(ImmutableList.of("A", "B", "C"))

    application.routing {
      get("/") {
        call.respond(model)
      }
      post("/") {
        val entity = call.receive<MultiMessage>()
        assertEquals(model, entity)
        call.respond(entity.toString())
      }
    }

    handleRequest(HttpMethod.Get, "/") {
      addHeader("Accept", contentType.toString())
    }.response.let { response ->
      assertEquals(HttpStatusCode.OK, response.status())
      assertNotNull(response.content)
      assertEquals(model, parseMultiMessageResponse(response))
      assertEquals(contentType, response.contentType().withoutParameters())
    }

    handleRequest(HttpMethod.Post, "/") {
      addHeader("Content-Type", contentType.toString())
      createMultiMessageRequest(model, this)
    }.response.let { response ->
      assertEquals(HttpStatusCode.OK, response.status())
      assertNotNull(response.content)
      assertEquals(
        listOf("""MultiMessage(content=[A, B, C])"""),
        response.content!!.lines()
      )
      assertEquals(ContentType.Text.Plain.withCharset(Charsets.UTF_8), response.contentType())
    }
  }

  @Test
  fun testMessageGeneric() = withTestApplication {
    application.install(ContentNegotiation) {
      register(contentType, contentConverter.apply {
        register(MessageString.serializer())
      })
    }

    val model = MessageString("A")

    application.routing {
      get("/") {
        call.respond(model)
      }
      post("/") {
        val entity = call.receive<MessageString>()
        assertEquals(model, entity)
        call.respond(entity.toString())
      }
    }

    handleRequest(HttpMethod.Get, "/") {
      addHeader("Accept", contentType.toString())
    }.response.let { response ->
      assertEquals(HttpStatusCode.OK, response.status())
      assertNotNull(response.content)
      assertEquals(model, parseMessageGenericResponse(response))
      assertEquals(contentType, response.contentType().withoutParameters())
    }

    handleRequest(HttpMethod.Post, "/") {
      addHeader("Content-Type", contentType.toString())
      createMessageGenericRequest(model, this)
    }.response.let { response ->
      assertEquals(HttpStatusCode.OK, response.status())
      assertNotNull(response.content)
      assertEquals(
        listOf("""MessageString(content=A)"""),
        response.content!!.lines()
      )
      assertEquals(ContentType.Text.Plain.withCharset(Charsets.UTF_8), response.contentType())
    }
  }

  abstract fun createEntityRequest(entity: Entity, request: TestApplicationRequest)
  abstract fun parseEntityResponse(response: TestApplicationResponse): Entity

  abstract fun createMessageGenericRequest(message: MessageString, request: TestApplicationRequest)
  abstract fun parseMessageGenericResponse(response: TestApplicationResponse): MessageString

  abstract fun createMultiMessageRequest(multiMessage: MultiMessage, request: TestApplicationRequest)
  abstract fun parseMultiMessageResponse(response: TestApplicationResponse): MultiMessage

}

@Serializable
data class Entity(
  @ProtoNumber(1)
  val id: Int,
  @ProtoNumber(2)
  val name: String,
  @ProtoNumber(3)
  val children: List<Child>
)

@Serializable
data class Child(
  @ProtoNumber(1)
  val item: String,
  @ProtoNumber(2)
  val quantity: Int
)


@Serializable
data class MultiMessage(
  @Serializable(with = ImmutableListSerializer::class)
  val content: ImmutableList<String>
)

abstract class MessageGeneric<T> {
  abstract val content: T
}

@Serializable
data class MessageString(override val content: String) : MessageGeneric<String>()
