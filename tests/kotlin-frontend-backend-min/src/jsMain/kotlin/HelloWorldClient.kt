package com.cedarsoft.open.kfbmin

import kotlinx.coroutines.*
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.w3c.dom.Element
import org.w3c.xhr.XMLHttpRequest
import kotlin.browser.document
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Minimal example that makes a request to the KTOR server.
 * Shares the data classes with the server.
 *
 * @author Johannes Schneider ([js@cedarsoft.com](mailto:js@cedarsoft.com))
 */
@UnstableDefault
fun main(args: Array<String>) {
  println("Starting Client in Browser")

  getElementSafe("daElementId").textContent = "Loading...."

  val json = Json(configuration = JsonConfiguration.Default, context = serializersModule)

  //Get the message, then update it and get it again
  GlobalScope.launch {
    getMessage(json)
    delay(1_000)
    postMessage(json, "Updated from client")
    getMessage(json)
  }
}

private suspend fun getMessage(json: Json) {
  println("getting message")
  val jsonString = httpGet("hello-world")
  println("Got JSON: $jsonString")
  val helloWorldData = json.parse(HelloWorldData.serializer(), jsonString)

  getElementSafe("daElementId").textContent = helloWorldData.message
}

private suspend fun postMessage(json: Json, message: String) {
  println("Post")
  val helloWorldDataAsJson = json.stringify(HelloWorldData.serializer(), HelloWorldData(message))
  httpPutOrPost("hello-world", helloWorldDataAsJson, HTTPVerbs.PUT)
}

private fun getElementSafe(id: String): Element {
  val element = document.getElementById(id)

  if (element == null) {
    println("No element found for id <$id>")
    throw IllegalArgumentException("No element found for <$id>")
  }

  return element
}

suspend fun httpGet(url: String): String = suspendCoroutine { c ->
  val xhr = XMLHttpRequest()
  xhr.onreadystatechange = { _ -> statusHandler(xhr, c) }
  xhr.open("GET", url)
  xhr.send()
}

suspend fun httpPutOrPost(url: String, data: String, httpVerb: HTTPVerbs): String = suspendCoroutine { c ->
  when (httpVerb) {
    HTTPVerbs.POST, HTTPVerbs.PUT -> {
      val xhr = XMLHttpRequest()
      xhr.onreadystatechange = { _ -> statusHandler(xhr, c) }
      xhr.onerror = { _ -> println("Error: ${xhr.responseText}") }
      xhr.onabort = { _ -> println("Aborted: ${xhr.responseText}") }
      xhr.open(httpVerb.name, url, true)
      xhr.setRequestHeader("Content-type", "application/json; charset=utf-8")
      xhr.send(data)
    }
    else                          -> console.log("An unsupported verb was passed through to this function")
  }
}

fun statusHandler(xhr: XMLHttpRequest, coroutineContext: Continuation<String>) {
  if (xhr.readyState == XMLHttpRequest.DONE) {
    if (xhr.status / 100 == 2) {
      coroutineContext.resume(xhr.response as String)
    } else {
      coroutineContext.resumeWithException(RuntimeException("HTTP error: ${xhr.status}"))
    }
  }
}

enum class HTTPVerbs {
  POST,
  GET,
  PUT,
  UPDATE,
  DELETE
}
