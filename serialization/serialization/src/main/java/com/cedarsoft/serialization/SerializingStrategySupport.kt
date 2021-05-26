/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.serialization

import com.cedarsoft.version.Version
import com.cedarsoft.version.VersionRange
import java.util.Collections
import javax.annotation.Nonnull

/**
 * Support class for serializing strategies.
 *
 *
 * It is necessary to register the strategies using [.addStrategy] and add the
 * necessary version mappings.
 *
 * @param <T> the type
 * @param <D> as defined in SerializingStrategy
 * @param <S> as defined in SerializingStrategy
 */
class SerializingStrategySupport<T : Any, S : Any, D : Any, O : Any, I : Any>(versionRange: VersionRange) {
  private val strategies: MutableList<SerializingStrategy<out T, S, D, O, I>> = ArrayList()

  val versionMappings: VersionMappings<SerializingStrategy<out T, S, D, O, I>> = VersionMappings(versionRange)

  /**
   * Returns the strategy for the given id.
   * Attention: The returned strategy is not able to serialize all types of T. Handle with care depending on the id!
   *
   * @param id the id
   * @return the strategy with that id
   *
   * @throws NotFoundException if not strategy could be found
   */
  @Nonnull
  @Throws(NotFoundException::class)
  fun findStrategy(@Nonnull id: String): SerializingStrategy<out T, S, D, O, I> {
    for (strategy in strategies) {
      if (strategy.id == id) {
        return strategy
      }
    }
    throw NotFoundException("No strategy found for id <$id>")
  }

  /**
   * Returns the first strategy that supports serialization for the given object
   *
   * @param `object` the object
   * @return the strategy that
   * @throws NotFoundException if the strategy could not be found for the given object
   */
  @Throws(NotFoundException::class)
  fun <R : T> findStrategy(@Nonnull objectToSerialize: R): SerializingStrategy<R, S, D, O, I> {
    for (strategy in strategies) {
      if (strategy.supports(objectToSerialize)) {
        return strategy as SerializingStrategy<R, S, D, O, I>
      }
    }
    throw NotFoundException("No strategy found for object <$`objectToSerialize`>")
  }

  /**
   * Returns the strategies
   *
   * @return the strategies
   */
  @Nonnull
  fun getStrategies(): Collection<SerializingStrategy<out T, S, D, O, I>> {
    return Collections.unmodifiableList(strategies)
  }

  @Nonnull
  fun addStrategy(@Nonnull strategy: SerializingStrategy<out T, S, D, O, I>): VersionMapping {
    strategies.add(strategy)
    return versionMappings.add(strategy, strategy.formatVersionRange)
  }

  @Nonnull
  fun resolveVersion(@Nonnull key: SerializingStrategy<out T, S, D, O, I>, @Nonnull version: Version): Version {
    return versionMappings.resolveVersion(key, version)
  }

  /**
   * Verifies the serializing strategy support
   *
   * @return the support
   */
  fun verify(): Boolean {
    if (strategies.isEmpty()) {
      throw SerializationException(SerializationException.Details.NO_STRATEGIES_AVAILABLE)
    }
    versionMappings.verify()
    return true
  }

}
