package it.neckar.react.common

import it.neckar.common.featureflags.FeatureFlag
import react.*


/**
 * This component throws an exception
 */
val BuggyComponent: FC<BuggyComponentProps> = fc("BuggyComponent") { props ->
  if (props.throwException == true) {
    throw IllegalStateException(props.message ?: "Exception thrown within BuggyComponent")
  }
}

external interface BuggyComponentProps : Props {
  var throwException: Boolean?
  var message: String?
}

/**
 * Uses the feature flags to decide whether to throw an exception
 */
val BuggyComponentFromFeatureFlags: FC<BuggyComponentFromFeatureFlagsProps> = fc("BuggyComponentFromFeatureFlags") { props ->
  val featureFlag = props.featureFlag ?: FeatureFlag.throwException

  val featureFlagsContextContent = useFeatureFlagsContext()

  BuggyComponent {
    attrs {
      throwException = featureFlagsContextContent.featureFlags.contains(featureFlag)
    }
  }
}

external interface BuggyComponentFromFeatureFlagsProps : Props {
  var featureFlag: FeatureFlag?
}
