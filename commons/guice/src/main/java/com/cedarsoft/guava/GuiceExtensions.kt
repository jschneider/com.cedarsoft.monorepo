package com.cedarsoft.guava

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Singleton
import com.google.inject.binder.AnnotatedBindingBuilder
import com.google.inject.binder.LinkedBindingBuilder
import com.google.inject.binder.ScopedBindingBuilder
import java.util.ArrayList
import javax.inject.Inject
import javax.inject.Provider


/**
 * Contains extension methods for Guice
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */

open class GuiceInjectorBuilder() {
  private val collected = ArrayList<Module>()

  fun module(config: Binder.() -> Any?): Module {
    return Module {
      binder ->
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

fun ScopedBindingBuilder.asSingleton() = `in`(Singleton::class.java)

inline fun <reified T> Binder.bind() = bind(T::class.java)!!

inline fun <reified T> AnnotatedBindingBuilder<in T>.to() = to(T::class.java)!!

inline fun <reified T> AnnotatedBindingBuilder<in T>.toSingleton() = to(T::class.java)!!.asSingleton()

inline fun <reified T> Injector.getInstance() = getInstance(T::class.java)!!

inline fun <reified T> Injector.getProvider() = getProvider(T::class.java)!!


inline fun <reified T> Binder.getProvider(): com.google.inject.Provider<T> = getProvider(T::class.java)


inline fun <T, reified S : Provider<out T>> LinkedBindingBuilder<T>.toProvider() = toProvider(S::class.java)
