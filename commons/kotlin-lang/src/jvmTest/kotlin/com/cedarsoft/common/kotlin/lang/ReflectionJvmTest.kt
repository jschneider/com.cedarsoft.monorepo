package com.cedarsoft.common.kotlin.lang

import assertk.*
import assertk.assertions.*
import kotlin.test.Test

class ReflectionTest {
  @Test
  fun testReflection() {
    val myClass = MyClass("Martha", 155)

    assertThat(myClass.name).isEqualTo("Martha")
    assertThat(myClass.age).isEqualTo(155)

    assertThat(myClass::class.simpleName).isEqualTo("MyClass")

    val props = props(myClass)
    assertThat(props.entries).hasSize(2)

    assertThat(props["name"]).isEqualTo("Martha")
    assertThat(props["age"]).isEqualTo(155)
  }
}

class MyClass(
  val name: String,
  age: Int,
) : MySuperClass(age)

open class MySuperClass(
  val age: Int,
)
