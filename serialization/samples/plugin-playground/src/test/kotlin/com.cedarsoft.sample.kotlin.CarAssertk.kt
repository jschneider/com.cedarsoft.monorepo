import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.cedarsoft.sample.Model
import com.cedarsoft.sample.Money

fun Assert<com.cedarsoft.sample.kotlin.Car>.hasModel(expected: Model) = prop(com.cedarsoft.sample.kotlin.Car::model).isEqualTo(expected)
fun Assert<com.cedarsoft.sample.kotlin.Car>.hasBasePrice(expected: Money) = prop(com.cedarsoft.sample.kotlin.Car::basePrice).isEqualTo(expected)
