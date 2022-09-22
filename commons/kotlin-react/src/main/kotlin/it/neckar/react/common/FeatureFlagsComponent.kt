package it.neckar.react.common

import it.neckar.common.featureflags.FeatureFlag
import it.neckar.common.featureflags.FeatureFlagsSupport
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

val FeatureFlagsComponent: FC<FeatureFlagsComponentProps> = fc("FeatureFlagsComponent") { props ->
  val featureFlags = props.featureFlags ?: FeatureFlag.available

  val featureFlagStates =
    featureFlags.associateWith { flag ->
      useState(FeatureFlagsSupport.flags.contains(flag))
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
          writeFlagStatesToFlagSupport(featureFlagStates)
          FeatureFlagsSupport.writeToUrl()
        }
      }
    }

  }

}

/**
 * Writes the state of multiple Feature Flags to Feature Flags Support
 */
private fun writeFlagStatesToFlagSupport(flagStates: Map<FeatureFlag, StateInstance<Boolean>>) {
  flagStates.forEach { (featureFlag, flagState) ->
    FeatureFlagsSupport.set(featureFlag, flagState.value)
  }
}


external interface FeatureFlagsComponentProps : Props {
  var featureFlags: List<FeatureFlag>?
}
