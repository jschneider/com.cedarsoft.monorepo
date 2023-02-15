package com.cedarsoft.charting.api.sanitizing

import assertk.*
import assertk.assertions.*
import kotlin.test.Ignore
import kotlin.test.Test


external interface MyPerson {
  val name: String
  val age: Int
  val myDouble: Double
  val myBool: Boolean

  val type: PersonType
}

enum class PersonType {
  Option1,
  Option2,
}

class SanitizingTest {

  @Ignore
  @Test
  fun testWithUndefined() {
    val jsObject = js("{}")

    val myPerson = jsObject.unsafeCast<MyPerson>()

    //Undefined returned as null
    assertThat(myPerson.type).isEqualTo(null)
    assertThat(myPerson.name).isEqualTo(null)
    assertThat(myPerson.age).isEqualTo(null)
    assertThat(myPerson.myDouble).isEqualTo(null)
    assertThat(myPerson.myBool).isEqualTo(null)
  }


  @Ignore
  @Test
  fun testWithUndefinedWithSanitization() {
    val jsObject = js("{}")

    val myPerson = jsObject.unsafeCast<MyPerson>()

    assertThat {
      myPerson.type.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Possible values: Option1, Option2")
        contains("Enum")
        contains("undefined")
      }
    }

    assertThat {
      myPerson.name.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("String")
        contains("undefined")
      }
    }

    assertThat {
      myPerson.age.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Int")
        contains("undefined")
      }
    }

    assertThat {
      myPerson.myBool.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Boolean")
        contains("undefined")
      }
    }

    assertThat {
      myPerson.myDouble.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Double")
        contains("undefined")
      }
    }
  }

  @Ignore
  @Test
  fun testWithWrongValuesWithSanitization() {
    val jsObject = js("""{ 'name': 17, 'age': "asdf", 'myBool': 18, 'myDouble': 'abc', 'type': 'Option7' }""")

    val myPerson = jsObject.unsafeCast<MyPerson>()

    assertThat {
      myPerson.type.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Possible values: Option1, Option2")
        contains("Enum")
        contains("Option7")
      }
    }

    assertThat {
      myPerson.name.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("String")
        contains("17")
      }
    }

    assertThat {
      myPerson.age.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Int")
        contains("asdf")
      }
    }

    assertThat {
      myPerson.myBool.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Boolean")
        contains("18")
      }
    }

    assertThat {
      myPerson.myDouble.sanitize()
    }.isFailure().all {
      isInstanceOf(SanitizingFailedException::class)
      message().isNotNull().all {
        contains("Double")
        contains("abc")
      }
    }
  }

  @Ignore
  @Test
  fun testFilledObjectWithoutSanitization() {
    val jsObject = js("""{ 'name': 'MyName', 'age': 77, 'myDouble': 99.4, 'myBool': true, 'type': 'Option1' }""")

    val myPerson = jsObject.unsafeCast<MyPerson>()

    assertThat(myPerson.type).isEqualTo("Option1") //enums are returned as strings
    assertThat(myPerson.name).isEqualTo("MyName")
    assertThat(myPerson.age).isEqualTo(77)
    assertThat(myPerson.myDouble).isEqualTo(99.4)
    assertThat(myPerson.myBool).isTrue()
  }

  @Ignore
  @Test
  fun testFilledObjectWithSanitization() {
    val jsObject = js("""{ 'name': 'MyName', 'age': 77, 'myDouble': 99.4, 'myBool': true, 'type': 'Option1' }""")

    val myPerson = jsObject.unsafeCast<MyPerson>()

    assertThat(myPerson.type.sanitize()).isEqualTo(PersonType.Option1) //enums are returned as strings
    assertThat(myPerson.name.sanitize()).isEqualTo("MyName")
    assertThat(myPerson.age.sanitize()).isEqualTo(77)
    assertThat(myPerson.myDouble).isEqualTo(99.4)
    assertThat(myPerson.myBool.sanitize()).isTrue()
  }
}

fun jsTypeOf(o: Any): String {
  return js("typeof o")
}

