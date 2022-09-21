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

  /**
   * Adds a feature flag
   */
  fun add(additionalFlag: FeatureFlag) {
    this.flags = this.flags.withAdditional(additionalFlag)
  }

  /**
   * Removes a feature flag
   */
  fun remove(flag: FeatureFlag) {
    this.flags = this.flags.withRemovedFlag(flag)
  }

  /**
   * Sets the state of the given feature flag
   */
  fun set(flag: FeatureFlag, flagState: Boolean) {
    if (flagState) {
      add(flag)
    } else {
      remove(flag)
    }
  }

  /**
   * Parses the feature flags from the URL and sets the current flags
   */
  fun parseFromUrl() {
    /**
     * Parse feature flags URL
     */
    flags = getUrlParameter("featureFlags")?.let {
      FeatureFlags.decodeFromString(it)
    } ?: FeatureFlags.empty
  }

  /**
   * Writes the currently set Feature Flags into the URL. Does not refresh the page.
   */
  fun writeToUrl() {
    val flags = FeatureFlagsSupport.flags.encodeToString()
    val urlWithoutFeatureFlags = getUrlWithoutFeatureFlags(window.location.href)
    if (flags.isEmpty()){
      window.history.replaceState(null,"", urlWithoutFeatureFlags)
    }else{
      window.history.replaceState(null,"", "$urlWithoutFeatureFlags?featureFlags=$flags")
    }
  }

  /**
   * Returns a String without the feature Flags.
   */
  fun getUrlWithoutFeatureFlags(url : String) : String{
    return url.substringBefore("?featureFlags=")
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

//TODO move to kotlin-js
external fun unescape(s: String): String
