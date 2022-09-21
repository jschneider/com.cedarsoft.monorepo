package it.neckar.react.common

import it.neckar.common.featureflags.FeatureFlag
import it.neckar.react.common.form.*
import react.*
import react.dom.*


val FeatureFlagsComponentForm: FC<FeatureFlagsComponentFormProps> = fc("FeatureFlagsComponentForm") { props ->

  val flagsStates = props.flagsStates

  div {
    flagsStates.forEach { (featureFlag, flagState) ->
      div("col") {
        div("p-2") {
          checkbox(
            valueAndSetter = flagState,
            fieldName = featureFlag.key,
            title = "${featureFlag.key}: ${featureFlag.description}",
            editableStatus = EditableStatus.Editable,
          )
        }
      }
    }
  }
}

external interface FeatureFlagsComponentFormProps : Props {
  var flagsStates: Map<FeatureFlag, StateInstance<Boolean>>
}




