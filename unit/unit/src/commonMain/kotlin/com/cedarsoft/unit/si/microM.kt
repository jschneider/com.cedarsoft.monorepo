package com.cedarsoft.unit.si


import com.cedarsoft.unit.Name
import com.cedarsoft.unit.Symbol
import com.cedarsoft.unit.Unit
import com.cedarsoft.unit.prefix.micro
import com.cedarsoft.unit.quantity.Length

/**
 * Micrometer
 */
@Suppress("ClassName")
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Unit
@Length
@Symbol(microM.SYMBOL)
@Name("micrometer")
@micro(m::class)
annotation class microM {
  companion object {
    const val SYMBOL: String = "μm"
  }
}
