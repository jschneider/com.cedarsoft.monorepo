package com.cedarsoft.guava

import com.google.inject.Inject
import com.google.inject.Provider
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.logging.Logger


/**
 */
class GuiceExtensionTest {
  fun configureInjections(test: Boolean) = GuiceInjectorBuilder.injector {
    module {
      if (test) {
        bind<LoggingService>().toInstance(StdoutLoggingService())
        bind<AppService>().toInstance(TestAppService(getProvider(LoggingService::class.java)))
      } else {
        bind<LoggingService>().to<JdkLoggingService>().asSingleton()
        bind<AppService>().toSingleton<RealAppService>()
      }
    }.plus()
  }
}

abstract class LoggingService() {
  abstract fun info(message: String)
}

open class StdoutLoggingService() : LoggingService() {
  override fun info(message: String) = println("INFO: $message")
}

open class JdkLoggingService() : LoggingService() {
  @Inject open var jdkLogger: Logger? = null

  override fun info(message: String) = jdkLogger!!.info(message)
}

abstract class AppService(protected val mode: String) {
  protected abstract val logger: LoggingService

  fun run() {
    logger.info("Application started in '$mode' mode with ${logger.javaClass.getName()} logger")
    logger.info("Hello, World!")
  }
}

open class RealAppService
@Inject constructor(
  override val logger: LoggingService
) : AppService("real")

open class TestAppService(
  open val loggerProvider: Provider<LoggingService>
) : AppService("test") {

  override val logger: LoggingService
    get() = loggerProvider.get()
}
