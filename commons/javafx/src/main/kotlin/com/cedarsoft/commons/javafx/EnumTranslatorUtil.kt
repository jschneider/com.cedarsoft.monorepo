package com.cedarsoft.commons.javafx

import it.neckar.open.annotations.JavaFriendly
import javax.annotation.Nonnull

typealias EnumTranslatorLambda = (item: Enum<*>) -> String?

/**
 * Enum translator
 */
object EnumTranslatorUtil {
  /**
   * Fallback enum translator
   */
  private val fallback: EnumTranslatorLambda = { item ->
    System.err.println("No enum translation found for <$item>")
    item.name
  }

  /**
   * The enum translators. The first one that returns a result will be used
   */
  @Nonnull
  private val enumTranslatorsChain = mutableListOf(fallback)

  /**
   * Adds an enum translator at *first* position.
   * The given enum translator is used *first*
   */
  @JvmStatic
  @JavaFriendly
  fun addEnumTranslator(enumTranslator: EnumTranslator) {
    enumTranslator(enumTranslator::translate)
  }

  /**
   * Adds an enum translator
   */
  fun enumTranslator(enumTranslator: EnumTranslatorLambda) {
    enumTranslatorsChain.add(0, enumTranslator)
  }

  /**
   * Resets the chain. Just adds the fallback enum translator
   */
  @JvmStatic
  fun reset() {
    enumTranslatorsChain.clear()
    enumTranslatorsChain.add(fallback)
  }

  /**
   * Translates the num
   */
  @JvmStatic
  fun translate(item: Enum<*>): String {
    return enumTranslatorsChain.firstNotNullOf { it(item) }
  }

  /**
   * The enum translator that uses the translator chain
   */
  @JvmStatic
  val enumTranslator: EnumTranslator = object : EnumTranslator {
    override fun translate(item: Enum<*>): String {
      return EnumTranslatorUtil.translate(item)
    }
  }
}
