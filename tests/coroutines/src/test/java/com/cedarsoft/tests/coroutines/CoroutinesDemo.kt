package com.cedarsoft.tests.coroutines


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
  CoroutinesDemo().run()
}

class CoroutinesDemo {

  fun run() {
    runBlocking {
      val channel = ConflatedBroadcastChannel("asdf")

      GlobalScope.async {
        delay(100)
        println("will set")
        channel.offer("SENT0")
        channel.offer("SENT1")
        channel.offer("SENT2")
        delay(100)
        channel.offer("SENT3")
        delay(100)
        channel.offer("SENT4")
        delay(100)
        channel.offer("SENT5")
        delay(100)
        channel.offer("SENT6")
        delay(100)
        channel.offer("SENT7")
        println("did set")
      }



      GlobalScope.async {
        val subscription = channel.openSubscription()

        while (true) {
          delay(200)

          val value = subscription.receiveOrNull()
          println("got value: $value")
        }
      }

      delay(4000)
    }
    println("done...")
  }

  private fun println(message: String) {
    logger.info(message)
  }

  companion object {
    val logger: Logger = LoggerFactory.getLogger(CoroutinesDemo::class.qualifiedName)
  }
}

// Message types for counterActor
sealed class CounterMsg

object IncCounter : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply
