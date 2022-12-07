package it.neckar.react.common

import js.core.jso
import react.*
import react.dom.*

/**
 * Provides the falback component for a given throwable and error info (which is usually null)
 */
typealias FallbackComponentProvider = (Throwable?, ErrorInfo?) -> ReactNode


external interface ErrorBoundaryState : State {
  /**
   * Is set to true if an error has been detected
   */
  var hasError: Boolean?

  /**
   * The throwable that has been caught (updated by react magic in RStatics)
   */
  var throwable: Throwable?

  /**
   * The error info that has been caught (updated by react magic in RStatics)
   */
  var errorInfo: ErrorInfo?
}

external interface ErrorBoundaryProps : PropsWithChildren {
  var fallbackComponent: FallbackComponentProvider
}

/**
 * Creates an error boundary that visualizes the error message
 */
class ErrorBoundary : RComponent<ErrorBoundaryProps, ErrorBoundaryState>() {
  /**
   * Magic companion object that allows catching of errors by this component
   * https://reactjs.org/docs/error-boundaries.html
   */
  companion object : RStatics<ErrorBoundaryProps, ErrorBoundaryState, ErrorBoundary, Nothing>(ErrorBoundary::class) {
    init {
      getDerivedStateFromError = {
        jso {
          hasError = true
          throwable = it
        }
      }
    }
  }

  override fun componentDidCatch(error: Throwable, info: ErrorInfo) {
    //noop
  }

  override fun RBuilder.render() {
    if (state.hasError == true) {
      // Render the fallback component if there is an error
      props.fallbackComponent.invoke(state.throwable, state.errorInfo).let {
        child(it)
      }
    } else {
      //Just the children - no error detected
      props.children()
    }
  }
}

/**
 * Creates an error boundary
 */
fun RBuilder.errorBoundary(
  fallbackComponent: FallbackComponentProvider = { throwable, errorInfo ->
    ErrorFallbackComponent.create {
      this.throwable = throwable
      this.errorInfo = errorInfo
    }
  },
  handler: RHandler<Props>,
): Unit = child(ErrorBoundary::class) {
  attrs {
    this.fallbackComponent = fallbackComponent
  }
  handler()
}

/**
 * Very simple component that visualizes an error. Usually a (visually) better component should be provided
 */
val ErrorFallbackComponent: FC<ErrorFallbackComponentProps> = fc("ErrorFallbackComponent") { props ->
  val throwable: Throwable? = props.throwable
  val errorInfo: ErrorInfo? = props.errorInfo

  div {
    h3 {
      +"Error detected: ${throwable?.message}"
    }
    throwable?.let {
      p {
        +"Throwable class: ${it::class}"
      }
    }
    throwable?.let {
      p {
        +"Throwable message: ${it.message}"
      }
    }
    errorInfo?.let {
      p {
        +"Error Info component stack: ${it.componentStack}"
      }
    }
  }
}

external interface ErrorFallbackComponentProps : Props {
  var throwable: Throwable?
  var errorInfo: ErrorInfo?
}
