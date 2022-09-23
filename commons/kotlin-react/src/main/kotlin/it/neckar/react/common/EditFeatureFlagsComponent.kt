package it.neckar.react.common

import it.neckar.common.featureflags.FeatureFlag
import it.neckar.common.featureflags.FeatureFlags
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * Renders and edits the feature flags from the [FeatureFlagsContext]
 */
val EditFeatureFlagsComponent: FC<FeatureFlagsComponentProps> = fc("FeatureFlagsComponent") { props ->
  val featureFlags = props.featureFlags ?: FeatureFlag.available

  val featureFlagsContextContent = useFeatureFlagsContext()

  val featureFlagStates: Map<FeatureFlag, StateInstance<Boolean>> = featureFlags.associateWith { flag ->
    useState(featureFlagsContextContent.featureFlags.contains(flag))
  }

  div {
    FeatureFlagsComponentForm {
      attrs {
        this.flagsStates = featureFlagStates
      }
    }

    button(classes = "btn btn-primary") {
      +"Anwenden"

      attrs {
        onClickFunction = {
          featureFlagsContextContent.updater(featureFlagStates.toFeatureFlags())
        }
      }
    }
  }
}

/**
 * Converts the map to a feature flags object
 */
private fun Map<FeatureFlag, StateInstance<Boolean>>.toFeatureFlags(): FeatureFlags {
  val flags = this
    .asSequence()
    .filter { it.value.value }
    .map {
      it.key
    }.toSet()

  return FeatureFlags(flags)
}

external interface FeatureFlagsComponentProps : Props {
  var featureFlags: List<FeatureFlag>?
}
