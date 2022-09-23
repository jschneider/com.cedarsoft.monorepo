package it.neckar.common.featureflags

import kotlinx.browser.window



/**
 * Writes the currently set Feature Flags into the URL. Does not refresh the page.
 */
fun FeatureFlagsSupport.writeToUrl() {
  val flags = FeatureFlagsSupport.flags.encodeToString()
  val urlWithoutFeatureFlags = FeatureFlagsSupport.getUrlWithoutFeatureFlags(window.location.href)
  if (flags.isEmpty()){
    window.history.replaceState(null,"", urlWithoutFeatureFlags)
  }else{
    window.history.replaceState(null,"", "$urlWithoutFeatureFlags?featureFlags=$flags")
  }
}

/**
 * Parses the feature flags from the URL and sets the current flags
 */
fun FeatureFlagsSupport.parseFromUrl() {
  /**
   * Parse feature flags URL
   */
  FeatureFlagsSupport.flags = getUrlParameter("featureFlags")?.let {
    FeatureFlags.decodeFromString(it)
  } ?: FeatureFlags.empty
}

/**
 * Returns a String without the feature Flags.
 */
fun FeatureFlagsSupport.getUrlWithoutFeatureFlags(url : String) : String{
  val result = url.substringBefore("&featureFlags=")
  return result.substringBefore("?featureFlags=")
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
