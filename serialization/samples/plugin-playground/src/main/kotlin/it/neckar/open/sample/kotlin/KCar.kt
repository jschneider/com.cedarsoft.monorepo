package com.cedarsoft.sample.kotlin

import com.cedarsoft.sample.Extra
import com.cedarsoft.sample.Model
import com.cedarsoft.sample.Money
import java.util.*

class Car @JvmOverloads constructor(
  val model: Model,
  val basePrice: Money,
  extras: Collection<Extra> = emptyList()
) {
  private val extras: MutableList<Extra> = mutableListOf<Extra>().apply {
    addAll(extras)
  }

  fun addExtra(extra: Extra) {
    extras.add(extra)
  }

  fun getExtras(): List<Extra> {
    return Collections.unmodifiableList(extras)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Car

    if (model != other.model) return false
    if (basePrice != other.basePrice) return false
    if (extras != other.extras) return false

    return true
  }

  override fun hashCode(): Int {
    var result = model.hashCode()
    result = 31 * result + basePrice.hashCode()
    result = 31 * result + extras.hashCode()
    return result
  }
}
