package it.neckar.common.featureflags

import it.neckar.commons.kotlin.js.LocalStorageKey
import it.neckar.commons.kotlin.js.LocalStorageSupport
import it.neckar.commons.kotlin.js.exception.unescape
import kotlinx.browser.window
import kotlinx.serialization.builtins.serializer


/**
 * The URL parameter name for feature flags
 */
const val FeatureFlagsUrlParam: String = "featureFlags"

/**
 * Writes the currently set Feature Flags into the URL. Does not refresh the page.
 */
fun FeatureFlagsSupport.writeToUrl() {
  flags.writeToUrl()
}

/**
 * Writes the feature flags into the URL. Does not refresh the page.
 */
fun FeatureFlags.writeToUrl() {
  val flagsAsString = encodeToString()
  val urlWithoutFeatureFlags = getUrlWithoutFeatureFlags(window.location.href)
  if (flagsAsString.isEmpty()) {
    window.history.replaceState(null, "", urlWithoutFeatureFlags)
  } else {
    window.history.replaceState(null, "", "$urlWithoutFeatureFlags?$FeatureFlagsUrlParam=$flagsAsString")
  }
}

/**
 * Parses the feature flags from the URL and sets the current flags
 */
fun FeatureFlagsSupport.parseFromUrl() {
  /**
   * Parse feature flags URL
   */
  this.flags = parseFeatureFlagsFromUrl() ?: FeatureFlags.empty
}

/**
 * Parses the feature flags from the URL.
 * Returns null if no url parameter could be found
 */
fun parseFeatureFlagsFromUrl(): FeatureFlags? {
  val urlParameter = getUrlParameter(FeatureFlagsUrlParam) ?: return null
  return FeatureFlags.decodeFromString(urlParameter)
}

/**
 * Returns a String without the feature Flags.
 */
fun getUrlWithoutFeatureFlags(url: String): String {
  val result = url.substringBefore("&$FeatureFlagsUrlParam=")
  return result.substringBefore("?$FeatureFlagsUrlParam=")
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

/**
 * Writes the feature flags to local storage
 */
fun FeatureFlags.writeToLocalStorage() {
  LocalStorageSupport.saveToLocalStorage(featureFlagsLocalStorageKey, this.encodeToString(), String.serializer())
}

/**
 * Loads the feature flags from local storage.
 * Returns null if no feature flags have been stored
 */
fun loadFeatureFlagsFromLocalStorage(): FeatureFlags? {
  val featureFlagsAsString = LocalStorageSupport.loadFromLocalStorage(featureFlagsLocalStorageKey, String.serializer()) ?: return null
  return FeatureFlags.decodeFromString(featureFlagsAsString)
}

val featureFlagsLocalStorageKey: LocalStorageKey = LocalStorageKey("featureFlags")


/**
 * Loads the feature flags from URL or local storage.
 *
 * Checks for feature flags in this order (returns the first hit):
 * - URL
 * - Local Storage
 *
 * Returns null if no feature flags could be found in any of the places
 */
fun loadFeatureFlagsFromUrlOrLocalStorage(): FeatureFlags? {
  //Load the feature flags from the url - initially
  val featureFlagsFromUrl = parseFeatureFlagsFromUrl()

  //if feature flags have been found, use these
  if (featureFlagsFromUrl != null) {
    return featureFlagsFromUrl
  }

  //load the feature flags from local storage
  val featureFlagsFromLocalStorage = loadFeatureFlagsFromLocalStorage()
  if (featureFlagsFromLocalStorage != null) {
    return featureFlagsFromLocalStorage
  }

  return null
}
