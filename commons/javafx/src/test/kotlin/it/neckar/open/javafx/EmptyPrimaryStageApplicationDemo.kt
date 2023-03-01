package it.neckar.open.javafx

import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.*

fun main() = runBlocking {
  println("Starting <EmptyPrimaryStageApplication>")

  launchEmptyPrimaryStageApplication()

  println("Waiting for future")
  EmptyPrimaryStageApplication.primaryStageReference.await().let {
    assertThat(it).isNotNull()
  }

  EmptyPrimaryStageApplication.primaryStageReference.getCompleted().let {
    assertThat(it).isNotNull()
  }

  println("done")
}
