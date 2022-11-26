package it.neckar.commons.logback

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.OutputStreamAppender
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy
import ch.qos.logback.core.spi.FilterReply
import ch.qos.logback.core.util.FileSize
import ch.qos.logback.core.util.StatusPrinter
import org.slf4j.LoggerFactory
import java.io.File
import java.io.PrintStream

/**
 * A configurer for logback logging.<br></br>
 *
 * @see [http://logback.qos.ch/](http://logback.qos.ch/)
 */
object LogbackConfigurer {

  fun configureLoggingConsoleOnly(levelForRoot: org.slf4j.event.Level) {
    configureLoggingConsoleOnly(levelForRoot.toLogback())
  }

  fun configureLoggingConsoleOnly(levelForRoot: ch.qos.logback.classic.Level = ch.qos.logback.classic.Level.INFO) {
    clearExistingAppenders()
    configureConsoleAppender()

    setRootLoggerLevel(levelForRoot)
  }

  /**
   * Configure logging
   */
  fun configureLoggingConsoleAndFile(logFile: File, levelForRoot: org.slf4j.event.Level) {
    configureLoggingConsoleAndFile(logFile, levelForRoot.toLogback())
  }

  /**
   * Configure (default) logging - for console and file
   */
  fun configureLoggingConsoleAndFile(logFile: File, levelForRoot: ch.qos.logback.classic.Level = ch.qos.logback.classic.Level.INFO) {
    clearExistingAppenders()
    configureConsoleAppender()
    configureFileAppender(logFile)

    setRootLoggerLevel(levelForRoot)
  }

  /**
   * Removes all existing appenders (e.g. from the BasicConfiguration)
   */
  fun clearExistingAppenders() {
    val rootLogger: Logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
    rootLogger.detachAndStopAllAppenders()
  }

  /**
   * Print the logger status to the given print stream
   */
  fun printStatus(out: PrintStream) {
    val lc = LoggerFactory.getILoggerFactory() as LoggerContext
    StatusPrinter.setPrintStream(out)
    StatusPrinter.print(lc)
  }

  private const val EncoderPattern: String = "[%d{ISO8601}]-[%thread] %-5level %logger - %msg%n"
  const val LogFileName: String = "application.log"

  /**
   * Suffix for logger that are only logged to file
   */
  const val LoggerSuffixFileOnly: String = "_fileOnly"
  const val FileAppenderName: String = "FILE"

  /**
   * Returns the logback classic Logger Context
   */
  val loggerContext: LoggerContext
    get() = LoggerFactory.getILoggerFactory() as LoggerContext

  /**
   * Returns the logback logger for the given name
   */
  private fun getLogbackLogger(loggerName: String): ch.qos.logback.classic.Logger = loggerContext.getLogger(loggerName)

  /**
   * Returns the corresponding logback logger from a slf4j logger
   */
  fun getLogbackLogger(logger: org.slf4j.Logger): ch.qos.logback.classic.Logger {
    return getLogbackLogger(logger.name)
  }

  /**
   * Sets the level for the root logger
   */
  fun setRootLoggerLevel(level: org.slf4j.event.Level) {
    setRootLoggerLevel(level.toLogback())
  }

  fun setRootLoggerLevel(level: ch.qos.logback.classic.Level) {
    val rootLogger = getLogbackLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    rootLogger.level = level
  }

  /**
   * Sets the logger leve for an slf4j logger
   */
  fun setLoggerLevel(logger: org.slf4j.Logger, level: org.slf4j.event.Level) {
    setLoggerLevel(logger.name, level)
  }

  /**
   * Sets the logger level for the logger with the given name
   */
  fun setLoggerLevel(loggerName: String, level: org.slf4j.event.Level) {
    val logger: ch.qos.logback.classic.Logger = getLogbackLogger(loggerName)
    setLoggerLevel(logger, level)
  }

  /**
   * Sets the logger level for a logback logger
   */
  fun setLoggerLevel(logger: ch.qos.logback.classic.Logger, level: org.slf4j.event.Level) {
    logger.level = level.toLogback()
  }

  fun getLoggerLevel(logger: ch.qos.logback.classic.Logger): org.slf4j.event.Level {
    return logger.level.toSlf4j()
  }

  fun getLoggerLevel(logger: org.slf4j.Logger): org.slf4j.event.Level {
    return logger.toLogback().level.toSlf4j()
  }

  /**
   * Creates and adds a file appender to the root logger.
   *
   * @param logFile the target file where to write the log messages
   */
  fun configureFileAppender(logFile: File) {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

    val fileAppender = RollingFileAppender<ILoggingEvent>()
    fileAppender.context = loggerContext
    fileAppender.name = FileAppenderName

    val rollingPolicy = FixedWindowRollingPolicy()
    rollingPolicy.minIndex = 1
    rollingPolicy.maxIndex = 3
    rollingPolicy.fileNamePattern = logFile.absolutePath + ".%i"
    rollingPolicy.context = loggerContext
    rollingPolicy.setParent(fileAppender)

    val triggeringPolicy = SizeBasedTriggeringPolicy<ILoggingEvent>()
    triggeringPolicy.setMaxFileSize(FileSize.valueOf("5 mb"))
    triggeringPolicy.context = loggerContext

    fileAppender.file = logFile.absolutePath
    fileAppender.isAppend = true // mandatory for RollingFileAppender
    fileAppender.rollingPolicy = rollingPolicy
    fileAppender.triggeringPolicy = triggeringPolicy

    rollingPolicy.start()
    triggeringPolicy.start()

    startAppenderAndAddToRoot(loggerContext, fileAppender)
  }

  /**
   * Creates and adds a console appender to the root logger.
   */
  fun configureConsoleAppender() {
    val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext

    val consoleAppender = ConsoleAppender<ILoggingEvent>()
    consoleAppender.context = loggerContext
    consoleAppender.name = "STDOUT"

    consoleAppender.addFilter(object : Filter<ILoggingEvent>() {
      override fun decide(event: ILoggingEvent): FilterReply {
        return if (event.loggerName.endsWith(LoggerSuffixFileOnly)) {
          FilterReply.DENY
        } else FilterReply.NEUTRAL
      }
    })

    startAppenderAndAddToRoot(loggerContext, consoleAppender)
  }

  private fun startAppenderAndAddToRoot(loggerContext: LoggerContext, appender: OutputStreamAppender<ILoggingEvent>) {
    val encoder = PatternLayoutEncoder()
    encoder.context = loggerContext
    encoder.pattern = EncoderPattern
    encoder.start()

    appender.encoder = encoder
    appender.start()

    val rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    rootLogger.addAppender(appender)
  }
}

fun ch.qos.logback.classic.Level.toSlf4j(): org.slf4j.event.Level {
  return when (this) {
    ch.qos.logback.classic.Level.ERROR -> org.slf4j.event.Level.ERROR
    ch.qos.logback.classic.Level.WARN -> org.slf4j.event.Level.WARN
    ch.qos.logback.classic.Level.INFO -> org.slf4j.event.Level.INFO
    ch.qos.logback.classic.Level.DEBUG -> org.slf4j.event.Level.DEBUG
    ch.qos.logback.classic.Level.TRACE -> org.slf4j.event.Level.TRACE
    else -> org.slf4j.event.Level.INFO
  }
}

/**
 * Converts a slf4j level to a logback level
 */
fun org.slf4j.event.Level.toLogback(): ch.qos.logback.classic.Level {
  return when (this) {
    org.slf4j.event.Level.ERROR -> ch.qos.logback.classic.Level.ERROR
    org.slf4j.event.Level.WARN -> ch.qos.logback.classic.Level.WARN
    org.slf4j.event.Level.INFO -> ch.qos.logback.classic.Level.INFO
    org.slf4j.event.Level.DEBUG -> ch.qos.logback.classic.Level.DEBUG
    org.slf4j.event.Level.TRACE -> ch.qos.logback.classic.Level.TRACE
  }
}

/**
 * Returns the corresponding logback logger
 */
fun org.slf4j.Logger.toLogback(): Logger {
  return LogbackConfigurer.getLogbackLogger(this)
}

/**
 * Allows setting the level on the logger directly
 */
var org.slf4j.Logger.level: org.slf4j.event.Level
  get() {
    return LogbackConfigurer.getLoggerLevel(this)
  }
  set(level) {
    LogbackConfigurer.setLoggerLevel(this, level)
  }
