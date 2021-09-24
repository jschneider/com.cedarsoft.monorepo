package it.neckar.common.featureflags

import kotlinx.browser.window

/**
 * Handles the feature flags
 */
object FeatureFlagsSupport {
  /**
   * Global variable containing the feature flags
   */
  var flags: FeatureFlags = FeatureFlags.empty
    private set

  init {
    //Initialize feature set

    flags = when {
      //Fallback for compatibility reasons
      getUrlParameter("alpha") == "true" -> {
        FeatureFlags.alpha
      }
      else -> {
        /**
         * Parse feature flags URL
         */
        getUrlParameter("featureFlags")?.let {
          FeatureFlags.decodeFromString(it)
        } ?: FeatureFlags.empty
      }
    }
  }
}

/**
 * Returns the URL parameter
 */
fun getUrlParameter(paramName: String): String? {
  val searchString = window.location.search.substring(1)
  val params = searchString.split("&")
  params.forEach {
    val parts = it.split("=")
    if (parts.size == 2 && parts[0] == paramName) {
      return unescape(parts[1])
    }
  }
  return null
}

external fun unescape(s: String): String
