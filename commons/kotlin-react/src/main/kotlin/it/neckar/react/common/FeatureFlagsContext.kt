package it.neckar.react.common

import it.neckar.common.featureflags.FeatureFlags
import it.neckar.common.featureflags.FeatureFlagsSupport
import it.neckar.common.featureflags.writeToUrl
import it.neckar.react.common.annotations.*
import react.*

/**
 * The context for feature flags
 */
val FeatureFlagsContext: Context<FeatureFlagsContextContent> = createContext(
  FeatureFlagsContextContent(
    FeatureFlagsSupport.flags,
    updater = {},
  )
)

@UsesHooks
fun useFeatureFlagsContext(): FeatureFlagsContextContent {
  return useContext(FeatureFlagsContext)
}

data class FeatureFlagsContextContent(
  val featureFlags: FeatureFlags,
  /**
   * Can be called to update the feature flags
   */
  val updater: (FeatureFlags) -> Unit,
)

/**
 * Provides the feature flags context
 */
val FeatureFlagsContextComponent: FC<FeatureFlagsContextComponentProps> = fc("FeatureFlagsContextComponent") { props ->
  val featureFlagsState = useState(FeatureFlagsSupport.flags)
  val updater: (FeatureFlags) -> Unit = useCallback() { updatedFeatureFlags ->
    //Set the feature flags globally
    FeatureFlagsSupport.flags = updatedFeatureFlags
    FeatureFlagsSupport.writeToUrl()
    //Update the state to trigger a rerender
    featureFlagsState.setter(updatedFeatureFlags)
  }

  FeatureFlagsContext.Provider(FeatureFlagsContextContent(FeatureFlagsSupport.flags, updater)) {
    props.children()
  }
}

external interface FeatureFlagsContextComponentProps : PropsWithChildren {
}
