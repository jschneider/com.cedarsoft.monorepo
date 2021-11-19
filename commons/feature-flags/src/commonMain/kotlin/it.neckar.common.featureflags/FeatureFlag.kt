package it.neckar.common.featureflags

/**
 * Describes a feature flag
 */
data class FeatureFlag(
  val key: String,
  /**
   * The *optional* description.
   * The description is only used for documentation issues. It will not be transferred or stored
   */
  val description: String? = null
) {

  init {
    require(!key.contains(",")) {
      "Key must not contain \",\""
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is FeatureFlag) return false

    if (key != other.key) return false

    return true
  }

  override fun hashCode(): Int {
    return key.hashCode()
  }

  companion object {
    /**
     * Marker for alpha version. Should not be used directly. Instead, create other feature flags
     */
    val alpha: FeatureFlag = FeatureFlag("alpha", "Marker for the alpha feature")

    /**
     * Simulate a delay in UI actions.
     * This feature can be used to test busy indicators.
     *
     * Every async action will be delayed for 1 sec
     */
    val slowUi: FeatureFlag = FeatureFlag("slowUI", "Delay actions in the UI")

    /**
     * Simulates a delay on HTTP requests responses on the server side
     */
    val slowServer: FeatureFlag = FeatureFlag("slowServer", "Delay responses on the server")

    /**
     * Enable / disable testing
     */
    val testing: FeatureFlag = FeatureFlag("testing", "Testing - non production")

    /**
     * Contains the available feature flags
     */
    val available: List<FeatureFlag> = listOf(alpha)

    /**
     * Returns the feature flag for the given key
     */
    fun forKey(key: String): FeatureFlag {
      return available.find {
        it.key == key
      } ?: FeatureFlag(key)
    }
  }
}
