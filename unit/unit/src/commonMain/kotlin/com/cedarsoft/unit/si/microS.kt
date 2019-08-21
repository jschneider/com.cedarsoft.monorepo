package com.cedarsoft.unit.si



import com.cedarsoft.unit.Name
import com.cedarsoft.unit.Symbol
import com.cedarsoft.unit.Unit
import com.cedarsoft.unit.prefix.micro
import com.cedarsoft.unit.quantity.Time

/**
 * micro seconds
 */
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Suppress("ClassName")
@Unit
@Time
@Symbol(microS.SYMBOL)
@Name("microsecond")
@micro(s::class)
annotation class microS {
  companion object {
    const val SYMBOL: String = "μs"
  }
}
