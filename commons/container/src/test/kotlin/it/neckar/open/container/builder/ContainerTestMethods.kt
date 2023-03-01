package it.neckar.open.container.builder

import com.google.common.io.ByteSource
import java.io.ByteArrayOutputStream

/**
 * Common container related test methods
 */
fun testContainer(config: ContainerTestConfiguration.() -> Unit) {
  val configuration = ContainerTestConfiguration().also(config)

  // Create the container
  val out = ByteArrayOutputStream().use {
    ZippedContainerBuilder(it).use {
      configuration._builder(it)
    }
    it
  }

  //Verify the container
  configuration._verifier?.invoke(ByteSource.wrap(out.toByteArray()))

  //Read the container
  out.toByteArray().inputStream().use {
    val reader = ZippedContainerReader(it)

    configuration._reader(reader)
  }
}

class ContainerTestConfiguration {
  @Suppress("PropertyName")
  var _builder: (ZippedContainerBuilder) -> Unit = {}

  @Suppress("PropertyName")
  var _verifier: ((container: ByteSource) -> Unit)? = null

  @Suppress("PropertyName")
  var _reader: (reader: ZippedContainerReader) -> Unit = {}

  fun build(builder: (builder: ZippedContainerBuilder) -> Unit) {
    this._builder = builder
  }

  fun verify(verifier: (container: ByteSource) -> Unit) {
    this._verifier = verifier
  }

  fun read(reader: (reader: ZippedContainerReader) -> Unit) {
    this._reader = reader
  }
}
