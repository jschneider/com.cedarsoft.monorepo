package it.neckar.react.common.redux

import react.redux.*
import redux.RAction

/**
 * Helper method to simplify usage of the [useDispatch] hook - without having to define the generics.
 */
inline fun useDispatch(): (RAction) -> dynamic {
  return react.redux.useDispatch()
}
