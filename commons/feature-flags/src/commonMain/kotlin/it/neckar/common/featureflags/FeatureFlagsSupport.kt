package it.neckar.common.featureflags

/**
 * Handles the feature flags
 */
object FeatureFlagsSupport {
  /**
   * Global variable containing the feature flags
   */
  var flags: FeatureFlags = FeatureFlags.empty

  /**
   * Adds a feature flag
   */
  fun add(additionalFlag: FeatureFlag) {
    flags = flags.withAdditional(additionalFlag)
  }

  /**
   * Removes a feature flag
   */
  fun remove(flag: FeatureFlag) {
    flags = flags.withRemovedFlag(flag)
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
}
