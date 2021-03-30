package com.cedarsoft.commons.javafx

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import javafx.stage.StageStyle
import kotlinx.coroutines.*

/**
 * Represents an empty primary stage
 */
class EmptyPrimaryStageApplication : Application() {
  override fun start(primaryStage: Stage) {
    primaryStage.initStyle(StageStyle.UNDECORATED)
    primaryStage.title = javaClass.simpleName

    (instance as CompletableDeferred).complete(this)
    (primaryStageReference as CompletableDeferred).complete(primaryStage)
  }

  companion object {
    /**
     * Provides the instance to the application
     */
    val instance: Deferred<EmptyPrimaryStageApplication> = CompletableDeferred()

    /**
     * A deferred that is completed as soon as the primary stage has been initialized
     */
    val primaryStageReference: Deferred<Stage> = CompletableDeferred()

    /**
     * Stops the empty primary stage application and returns as soon as the application has been stopped
     *
     */
    suspend fun stop() {
      if (!isFxApplicationThreadRunning()) {
        //Not running
        return
      }

      withContext(Dispatchers.Main) {
        primaryStageReference.getCompleted().hide()
      }

      Platform.exit()
    }
  }
}

/**
 * Launches the empty primary stage application.
 *
 * This method creates a single thread context and launches the JavaFX application
 */
fun CoroutineScope.launchEmptyPrimaryStageApplication(): Job {
  return this.launch(newSingleThreadContext("Empty Primary Stage Application Launcher")) {
    Application.launch(EmptyPrimaryStageApplication::class.java)
  }.also {
    runBlocking {
      EmptyPrimaryStageApplication.primaryStageReference.await()
    }
  }
}
