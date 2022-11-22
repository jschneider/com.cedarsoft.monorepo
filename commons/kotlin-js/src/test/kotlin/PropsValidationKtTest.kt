import assertk.*
import assertk.assertions.*
import com.cedarsoft.common.kotlin.lang.EnvironmentMode
import it.neckar.commons.kotlin.js.safeGet
import kotlin.test.Test

external interface MyPerson {
  var name: String
}

class PropsValidationKtTest {

  @Test
  fun testCorrectType() {
    val jsObject = js("""{ 'name': 'max'}""")

    val testPerson = jsObject.unsafeCast<MyPerson>()
    val propertyName = testPerson::name

    assertThat(propertyName.get()).isNotNull()
    assertThat(propertyName.get()).isInstanceOf(String::class)

    assertThat {
      propertyName.safeGet()
    }.isSuccess().isEqualTo("max")
  }

  @Test
  fun testWithUndefined() {
    val jsObject = js("{}")

    val testPerson = jsObject.unsafeCast<MyPerson>()
    val propertyName = testPerson::name

    assertThat(propertyName.get()).isEqualTo(null)
    assertThat(propertyName.get()).isNotInstanceOf(String::class)

    assertThat {
      propertyName.safeGet()
    }.isFailure().messageContains("is not set")
  }

  @Test
  fun testWithWrongType() {
    val jsObject = js("""{ 'name': 42}""")

    val testPerson = jsObject.unsafeCast<MyPerson>()
    val propertyName = testPerson::name

    assertThat(propertyName.get()).isNotNull()
    assertThat(propertyName.get()).isNotInstanceOf(String::class)

    assertThat {
      propertyName.safeGet()
    }.isFailure().messageContains("has invalid value")
  }

  @Test
  fun testWrongFieldName() {
    val jsObject = js("""{ 'invalidName': 'I Am Invalid'}""")

    val testPerson = jsObject.unsafeCast<MyPerson>()

    val propertyName = testPerson::name
    assertThat(propertyName.get()).isNull()

    assertThat {
      propertyName.safeGet()
    }.isFailure().messageContains("is not set")
  }
}
