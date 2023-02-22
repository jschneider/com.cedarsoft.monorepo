package it.neckar.react.common.loading

import com.cedarsoft.unit.other.pct
import com.cedarsoft.unit.other.pct100
import it.neckar.react.common.*
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.classes
import react.*
import react.dom.*
import styled.*
import web.timers.setTimeout
import kotlin.math.roundToInt


/**
 * Bootstrap progress bar with a custom color, animation is disabled
 * @param customColor enter an additional class to color the progress bar. Should start with 'bg-', e.g. bg-primary
 */
fun RBuilder.bootstrapProgressBarCustomColor(
  title: String,
  currentPercentage: @pct Double,
  customColor: String? = null,
  headerProgressBar: Boolean = false,
): Unit = child(bootstrapProgressBarCustomColor) {
  attrs {
    this.title = title
    this.currentPercentage = currentPercentage
    this.headerProgressBar = headerProgressBar
    this.customColor = customColor
  }
}

val bootstrapProgressBarCustomColor: FC<ProgressCustomColorProps> = fc("bootstrapProgressBarCustomColor") { props ->
  val customColor = props.customColor
  @pct val currentPercentage = props.currentPercentage
  @pct100 val currentPercentage100 = (currentPercentage * 100).roundToInt()

  val (visible, setVisible) = useState(true)

  //TODO is this the best way? Maybe use CSS to hide?
  @pct val percentageRef = useRef(currentPercentage)
  useEffect(currentPercentage) {
    percentageRef.current = currentPercentage

    if (props.headerProgressBar) {
      if (currentPercentage >= 1.0) {
        setTimeout({
          if ((percentageRef.current ?: 0.0) >= 1.0) {
            setVisible(false)
          }
        }, 300)
      } else {
        setVisible(true)
      }
    }
  }

  if (visible) {
    styledDiv {
      attrs {
        classes = setOf("progress")
      }
      css {
        if (props.headerProgressBar) height = 5.px
      }

      styledDiv {
        attrs {
          classes = if (customColor != null) {
            require(customColor.startsWith("bg-")) {
              "Progress Bar with a custom color requires an additional class which must start with 'bg-'. "
            }
            setOf("progress-bar", customColor)
          } else {
            setOf("progress-bar")
          }

          roleHTML = "progressbar"
          ariaValueRange = IntRange(0, 100)
          ariaValueNow = currentPercentage100
          ariaLabel = props.title
        }

        css {
          width = currentPercentage100.pct
          if (props.headerProgressBar) height = 5.px

          transition = Transitions.none
        }
      }
    }
  }
}

external interface ProgressCustomColorProps : Props {
  var title: String
  var currentPercentage: @pct Double
  var headerProgressBar: Boolean
  var customColor: String?
}
