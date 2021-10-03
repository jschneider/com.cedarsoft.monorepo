package it.neckar.common.featureflags

/**
 * Contains a set of feature flags
 */
data class FeatureFlags(
  val flags: Set<FeatureFlag>
) {
  /**
   * Returns true if this contains the given feature flag
   */
  fun contains(featureFlag: FeatureFlag): Boolean {
    return this.flags.contains(featureFlag)
  }

  constructor(vararg flags: FeatureFlag) : this(flags.toSet())

  override fun toString(): String {
    return "[${flags.joinToString(", ") { it.key }}]"
  }

  /**
   * Encodes the feature flags as string
   */
  fun encodeToString(): String {
    return flags.joinToString(",") { it.key }
  }

  companion object {
    /**
     * The HTTP header key used for feature flags
     */
    const val HeaderKey: String = "Feature-Flags"

    /**
     * Contains no feature flags
     */
    val empty: FeatureFlags = FeatureFlags(emptySet())

    /**
     * Contains a list of well known features for the alpha version
     */
    val alpha: FeatureFlags = FeatureFlags(FeatureFlag.alpha)


    /**
     * Parses the feature flags from a string created by [asString]
     */
    fun decodeFromString(asString: String?): FeatureFlags {
      if (asString == null) {
        return empty
      }

      return FeatureFlags(
        asString.split(",")
          .map {
            FeatureFlag.forKey(it)
          }.toSet()
      )
    }
  }
}