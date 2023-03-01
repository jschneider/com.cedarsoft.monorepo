package it.neckar.commons.logback

import it.neckar.open.app.ApplicationHomeAccess
import it.neckar.open.app.ApplicationHomeAccessFactory
import it.neckar.open.io.FileUtils
import org.slf4j.event.Level
import java.io.File

/**
 * Configures logback using application home.
 *
 * ATTENTION: Projects that use this class must add a dependency to `Projects.open_commons_app` manually:
 * `implementation(project(Projects.open_commons_app))`
 */
class ApplicationHomeLogbackConfigurer(val applicationName: String) {
  /**
   * Configure logging - console and application log file
   */
  fun configureLogging(level: Level = Level.INFO) {
    val logFile = getLogFile(applicationName)
    LogbackConfigurer.configureLoggingConsoleAndFile(logFile, level)
  }

  companion object {
    /**
     * Returns the log file for the application
     */
    fun getLogFile(applicationName: String): File {
      return getLogFile(ApplicationHomeAccessFactory.create(applicationName))
    }

    /**
     * Returns the log file within the given application home access
     */
    fun getLogFile(applicationHomeAccess: ApplicationHomeAccess): File {
      FileUtils.ensureDirectoryExists(applicationHomeAccess.cacheHome)
      return File(applicationHomeAccess.cacheHome, LogbackConfigurer.LogFileName)
    }
  }
}
