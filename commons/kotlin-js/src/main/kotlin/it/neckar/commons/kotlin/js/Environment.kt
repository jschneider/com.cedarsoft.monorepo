package it.neckar.commons.kotlin.js

import kotlinx.js.DEV
import kotlinx.js.PROD
import kotlinx.js.import


/**
 * Information about the environment
 */
object Environment {
  val Dev: Boolean
    get() {
      return import.meta.env.DEV
    }

  val Prod: Boolean
    get() {
      return import.meta.env.PROD
    }
}

