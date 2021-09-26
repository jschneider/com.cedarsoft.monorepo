package toast

import obj

object ToastInit {
  init {
    //TODO how to automatically load the css file?
    //require("toastr/build/toastr.min.css")
  }
}

/**
 * Toast message types.
 */
internal enum class ToastType(internal val type: String) {
  INFO("info"),
  SUCCESS("success"),
  WARNING("warning"),
  ERROR("error")
}

/**
 * Toast positions.
 */
enum class ToastPosition(internal val position: String) {
  TOPRIGHT("toast-top-right"),
  BOTTOMRIGHT("toast-bottom-right"),
  BOTTOMLEFT("toast-bottom-left"),
  TOPLEFT("toast-top-left"),
  TOPFULLWIDTH("toast-top-full-width"),
  BOTTOMFULLWIDTH("toast-bottom-full-width"),
  TOPCENTER("toast-top-center"),
  BOTTOMCENTER("toast-bottom-center")
}

/**
 * Toast easings.
 */
enum class ToastEasing(internal val easing: String) {
  SWING("swing"),
  LINEAR("linear")
}

/**
 * Toast animation methods.
 */
enum class ToastMethod(internal val method: String) {
  FADEIN("fadeIn"),
  FADEOUT("fadeOut"),
  SLIDEUP("slideUp"),
  SLIDEDOWN("slideDown"),
  SHOW("show"),
  HIDE("hide")
}

/**
 * Toast options.
 */
data class ToastOptions(
  val positionClass: ToastPosition? = null,
  val escapeHtml: Boolean? = null,
  val closeButton: Boolean? = null,
  val closeHtml: String? = null,
  val closeDuration: Int? = null,
  val newestOnTop: Boolean? = null,
  val showEasing: ToastEasing? = null,
  val hideEasing: ToastEasing? = null,
  val closeEasing: ToastEasing? = null,
  val showMethod: ToastMethod? = null,
  val hideMethod: ToastMethod? = null,
  val closeMethod: ToastMethod? = null,
  val preventDuplicates: Boolean? = null,
  val timeOut: Int? = null,
  val extendedTimeOut: Int? = null,
  val progressBar: Boolean? = null,
  val rtl: Boolean? = null,
  val onShown: (() -> Unit)? = null,
  val onHidden: (() -> Unit)? = null,
  val onClick: (() -> Unit)? = null,
  val onCloseClick: (() -> Unit)? = null
)

internal fun ToastOptions.toJs(): dynamic {
  return obj {
    if (positionClass != null) this.positionClass = positionClass.position
    if (escapeHtml != null) this.escapeHtml = escapeHtml
    if (closeButton != null) this.closeButton = closeButton
    if (closeHtml != null) this.closeHtml = closeHtml
    if (closeDuration != null) this.closeDuration = closeDuration
    if (newestOnTop != null) this.newestOnTop = newestOnTop
    if (showEasing != null) this.showEasing = showEasing.easing
    if (hideEasing != null) this.hideEasing = hideEasing.easing
    if (closeEasing != null) this.closeEasing = closeEasing.easing
    if (showMethod != null) this.showMethod = showMethod.method
    if (hideMethod != null) this.hideMethod = hideMethod.method
    if (closeMethod != null) this.closeMethod = closeMethod.method
    if (preventDuplicates != null) this.preventDuplicates = preventDuplicates
    if (timeOut != null) this.timeOut = timeOut
    if (extendedTimeOut != null) this.extendedTimeOut = extendedTimeOut
    if (progressBar != null) this.progressBar = progressBar
    if (rtl != null) this.rtl = rtl
    if (onShown != null) this.onShown = onShown
    if (onHidden != null) this.onHidden = onHidden
    if (onClick != null) this.onclick = onClick
    if (onCloseClick != null) this.onCloseClick = onCloseClick
  }
}

/**
 * Toast component object.
 */
object Toast {

  /**
   * Shows a success toast.
   * @param message a toast message
   * @param title a toast title
   * @param options toast options
   */
  fun success(message: String, title: String? = null, options: ToastOptions? = null) {
    show(ToastType.SUCCESS, message, title, options)
  }

  /**
   * Shows an info toast.
   * @param message a toast message
   * @param title a toast title
   * @param options toast options
   */
  fun info(message: String, title: String? = null, options: ToastOptions? = null) {
    show(ToastType.INFO, message, title, options)
  }

  /**
   * Shows a warning toast.
   * @param message a toast message
   * @param title a toast title
   * @param options toast options
   */
  fun warning(message: String, title: String? = null, options: ToastOptions? = null) {
    show(ToastType.WARNING, message, title, options)
  }

  /**
   * Shows an error toast.
   * @param message a toast message
   * @param title a toast title
   * @param options toast options
   */
  fun error(message: String, title: String? = null, options: ToastOptions? = null) {
    show(ToastType.ERROR, message, title, options)
  }

  private fun show(type: ToastType, message: String, title: String? = null, options: ToastOptions? = null) {
    println("Showing Toast!!!!!!!")

    if (options != null) {
      toastr[type.type](message, title, options.toJs())
    } else {
      toastr[type.type](message, title)
    }
  }

}

external fun require(name: String): dynamic

internal val toastr = require("toastr")

