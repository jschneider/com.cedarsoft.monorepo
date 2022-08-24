package it.neckar.react.common

import it.neckar.react.common.toast.*
import kotlinx.coroutines.*

/**
 * App Scope - should be used as base scope
 */
val AppScope: CoroutineScope = CoroutineScope(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
  println("------------ EXCEPTION HANDLER ----------")
  println("Class: <${throwable::class.simpleName}>")
  println("Message: <${throwable.message}>")

  throwable.cause?.let { cause ->
    println("Cause.class: <${cause::class.simpleName}>")
    println("Cause.message: <${cause.message}>")
  }

  throwable.printStackTrace()
  println("------------ /EXCEPTION HANDLER ----------")

  when (throwable) {
    is Error -> {
      when (throwable.message) {
        "Fail to fetch" -> {
          Toast.error("Der Server ist aktuell nicht erreichbar", "Netzwerkfehler", ToastOptions(positionClass = ToastPosition.TOPCENTER, timeOut = 5000))
        }
        else -> Toast.error(
          "Ein Fehler ist aufgetreten:<br>${throwable.message}", "Fehler", ToastOptions(
            timeOut = 8000,
            positionClass = ToastPosition.BOTTOMCENTER,
          )
        )
      }
    }
    else -> Toast.error(
      "Ein Fehler ist aufgetreten:<br>${throwable.message}", "Fehler", ToastOptions(
        timeOut = 8000,
        positionClass = ToastPosition.BOTTOMCENTER,
      )
    )
  }
})
