package com.cedarsoft.commons.provider

/**
 * Provides coordinates.
 * Works like the [SizedProvider] but returns double values for each x and y.
 *
 * This class is an optimization that should be used to avoid boxing of double values.
 *
 * There exist variants for different number of parameters:
 * *[DoublesProvider1]: Takes one parameter
 */
interface CoordinatesProvider1<in P1> : HasSize1<P1>, MultiCoordinatesProvider1<SizedProviderIndex, P1> {
}
