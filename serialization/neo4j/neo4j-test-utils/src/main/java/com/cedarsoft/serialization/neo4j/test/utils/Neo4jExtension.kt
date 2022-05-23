package com.cedarsoft.serialization.neo4j.test.utils

import com.cedarsoft.serialization.neo4j.test.utils.Neo4jExtension.TemporaryGraphDatabase
import com.cedarsoft.test.utils.AbstractResourceProvidingExtension
import com.cedarsoft.test.utils.TemporaryFolder
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolutionException
import org.neo4j.graphdb.GraphDatabaseService
import org.neo4j.test.TestGraphDatabaseFactory
import java.io.File
import java.io.IOException
import java.lang.reflect.Parameter
import javax.annotation.Nonnull

/**
 * Use by annotating a test with [WithNeo4j]
 */
class Neo4jExtension : AbstractResourceProvidingExtension<TemporaryGraphDatabase>(TemporaryGraphDatabase::class.java) {
  override fun createResource(extensionContext: ExtensionContext): TemporaryGraphDatabase {
    return try {
      val temporaryFolder = TemporaryFolder()
      val db = createDb(temporaryFolder.newFolder())
      TemporaryGraphDatabase(temporaryFolder, db)
    } catch (e: IOException) {
      throw RuntimeException(e)
    }
  }

  @Throws(ParameterResolutionException::class)
  override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
    return if (super.supportsParameter(parameterContext, extensionContext)) {
      true
    } else GraphDatabaseService::class.java.isAssignableFrom(parameterContext.parameter.type)
  }

  @Throws(ParameterResolutionException::class, Exception::class)
  override fun convertResourceForParameter(parameter: Parameter, resource: TemporaryGraphDatabase): Any {
    if (GraphDatabaseService::class.java.isAssignableFrom(parameter.type)) {
      return resource.db
    }
    throw ParameterResolutionException("unable to resolve parameter for $parameter")
  }

  override fun cleanup(@Nonnull resource: TemporaryGraphDatabase) {
    resource.db.shutdown()
    resource.temporaryFolder.delete()
  }

  fun createDb(@Nonnull folder: File?): GraphDatabaseService {
    return TestGraphDatabaseFactory().newImpermanentDatabase(folder)
  }

  class TemporaryGraphDatabase(
    val temporaryFolder: TemporaryFolder,
    val db: GraphDatabaseService
  )
}
