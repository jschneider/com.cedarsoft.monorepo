package it.neckar.open.coroutines

import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Test


class CoroutinesExceptionTest {
  @Test
  fun testErrorHandler() = runTest {
    val errors = mutableListOf<Throwable>()

    val appScope = CoroutineScope(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
      errors.add(throwable)
    })

    assertThat(errors).isEmpty()

    appScope.launch {
      throw RuntimeException("Uuups")
    }.join()

    assertThat(errors).hasSize(1)
    assertThat(errors[0]).isInstanceOf(RuntimeException::class)


    //The result of deferred is *not* thrown, instead should be accessed using the deferred
    val deferred = appScope.async {
      throw IllegalStateException("bar")
    }

    assertThat(errors).hasSize(1)
    deferred.join()
    assertThat(errors).hasSize(1)
    assertThat(errors[0]).isInstanceOf(RuntimeException::class)
  }

  //@Test
  //fun testExceptionHandling(): Unit = runBlocking {
  //  val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
  //
  //  val job = scope.launch{
  //    throw IllegalStateException("Hello Exception")
  //  }
  //
  //  val deferred = scope.async {
  //    throw IllegalStateException("Hello Exception in async")
  //  }
  //
  //
  //  //assertThat(scope.isActive).isTrue()
  //  delay(100)
  //  assertThat(job.isActive).isFalse()
  //  //assertThat(scope.isActive).isTrue()
  //
  //
  //  println("-----------------")
  //  deferred.join()
  //  deferred.getCompleted()
  //}
}
