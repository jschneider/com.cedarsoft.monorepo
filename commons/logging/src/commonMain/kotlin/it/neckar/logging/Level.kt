package it.neckar.logging

/**
 * Represents a log level.
 * Inspired by slf4j
 */
expect enum class Level

/**
 * Returns true if *this* level is active compared
 * by the provided effective log level
 */
fun Level.isEnabled(effectiveLogLevel: Level): Boolean {
  return this <= effectiveLogLevel
}
