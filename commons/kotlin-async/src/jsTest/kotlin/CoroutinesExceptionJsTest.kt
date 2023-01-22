import assertk.*
import assertk.assertions.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import kotlin.js.Promise
import kotlin.test.Test


class CoroutinesExceptionJsTest {
  @Test
  fun testErrorHandler(): Promise<Unit> = runTest {
    val errors = mutableListOf<Throwable>()

    val appScope = CoroutineScope(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
      errors.add(throwable)
    })

    assertThat(errors).isEmpty()

    appScope.launch {
      throw RuntimeException("Uuups")
    }.join()

    assertThat(errors).hasSize(1)
    assertThat(errors.first()).isInstanceOf(RuntimeException::class)

    appScope.launch {
      throw Error("An error")
    }.join()

    assertThat(errors).hasSize(2)
    assertThat(errors.first()).isInstanceOf(RuntimeException::class)
    assertThat(errors[1]).isInstanceOf(Error::class)

    appScope.launch {
      throw js(""" "Empty!" """) //In JS everything can be thrown, even a String!
    }.join()

    assertThat(errors).hasSize(3)
    assertThat(errors.first()).isInstanceOf(RuntimeException::class)
    assertThat(errors[1]).isInstanceOf(Error::class)
    assertThat(errors[2]).isInstanceOf(String::class)
  }
}
