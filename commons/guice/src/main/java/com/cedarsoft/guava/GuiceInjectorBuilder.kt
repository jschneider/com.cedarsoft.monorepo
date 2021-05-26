package com.cedarsoft.guava

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Singleton
import com.google.inject.binder.AnnotatedBindingBuilder
import com.google.inject.binder.LinkedBindingBuilder
import com.google.inject.binder.ScopedBindingBuilder
import javax.inject.Provider


/**
 * Contains extension methods for Guice
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

open class GuiceInjectorBuilder {
  private val collected = ArrayList<Module>()

  fun module(config: Binder.() -> Any?): Module {
    return Module { binder ->
      binder.config()
    }
  }

  fun Module.plus() {
    collected.add(this)
  }

  companion object {
    fun injector(config: GuiceInjectorBuilder.() -> Any?): Injector {
      val collector = GuiceInjectorBuilder()
      collector.config()
      return Guice.createInjector(collector.collected)
    }
  }
}

fun ScopedBindingBuilder.asSingleton(): Unit = `in`(Singleton::class.java)

inline fun <reified T> Binder.bind(): AnnotatedBindingBuilder<T> = bind(T::class.java)!!

inline fun <reified T> AnnotatedBindingBuilder<in T>.to(): ScopedBindingBuilder = to(T::class.java)!!

inline fun <reified T> AnnotatedBindingBuilder<in T>.toSingleton(): Unit = to(T::class.java)!!.asSingleton()

inline fun <reified T> Injector.getInstance(): T = getInstance(T::class.java)!!

inline fun <reified T> Injector.getProvider(): Provider<T> = getProvider(T::class.java)!!


inline fun <reified T> Binder.getProvider(): com.google.inject.Provider<T> = getProvider(T::class.java)


inline fun <T, reified S : Provider<out T>> LinkedBindingBuilder<T>.toProvider(): ScopedBindingBuilder = toProvider(S::class.java)
