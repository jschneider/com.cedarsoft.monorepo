package com.cedarsoft.test.utils

import com.cedarsoft.common.time.FixedNowProvider
import com.cedarsoft.common.time.NowProvider
import com.cedarsoft.common.time.nowProvider
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import java.io.IOException
import java.lang.reflect.Parameter
import javax.annotation.Nonnull

/**
 * Extension that provides the fixed now provider as parameter.
 * Use [FixedTime] at the class/method and add [FixedNowProvider] to the test method parameters
 */
class FixedNowProviderExtension : AbstractResourceProvidingExtension<FixedNowProvider>(FixedNowProvider::class.java), BeforeEachCallback, AfterEachCallback, BeforeAllCallback, AfterAllCallback {

  private val configuringSupport: ConfiguringSupport<NowProvider, FixedTime> = ConfiguringSupport(NowProvider::class.java, FixedTime::class.java, "fixedTime", object : ConfigurationCallback<NowProvider, FixedTime> {
    override fun getOriginalValue(): NowProvider {
      return nowProvider
    }

    override fun extract(annotation: FixedTime): NowProvider? {
      return FixedNowProvider(annotation.value)
    }

    override fun applyValue(value: NowProvider) {
      nowProvider = value
    }
  })


  override fun beforeAll(extensionContext: ExtensionContext) {
    configuringSupport.beforeAll(extensionContext)
  }

  override fun afterAll(extensionContext: ExtensionContext) {
    configuringSupport.afterAll(extensionContext)
  }

  override fun beforeEach(extensionContext: ExtensionContext) {
    configuringSupport.beforeEach(extensionContext)
  }

  override fun afterEach(extensionContext: ExtensionContext) {
    configuringSupport.afterEach(extensionContext)
  }

  @Nonnull
  override fun createResource(extensionContext: ExtensionContext): FixedNowProvider {
    return nowProvider as? FixedNowProvider ?: throw IllegalStateException("Invalid instance of nowProvider. Expected <FixedNowProvider> but was <$nowProvider>")
  }

  @Throws(ParameterResolutionException::class)
  override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
    if (super.supportsParameter(parameterContext, extensionContext)) {
      return true
    }

    return parameterContext.parameter.isAnnotationPresent(FixedTime::class.java)
  }

  @Nonnull
  @Throws(ParameterResolutionException::class, IOException::class)
  override fun convertResourceForParameter(@Nonnull parameter: Parameter, @Nonnull resource: FixedNowProvider): Any {
    return resource
  }

  override fun cleanup(@Nonnull resource: FixedNowProvider) {
  }
}
