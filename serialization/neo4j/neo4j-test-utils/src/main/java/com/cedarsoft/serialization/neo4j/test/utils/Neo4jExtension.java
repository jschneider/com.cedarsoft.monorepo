package com.cedarsoft.serialization.neo4j.test.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.extension.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import com.cedarsoft.test.utils.AbstractResourceProvidingExtension;
import com.cedarsoft.test.utils.TemporaryFolder;

/**
 * Use by annotating a test with {@link WithNeo4j}
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class Neo4jExtension extends AbstractResourceProvidingExtension<Neo4jExtension.TemporaryGraphDatabase> {

  public Neo4jExtension() {
    super(TemporaryGraphDatabase.class);
  }

  @Nonnull
  @Override
  protected TemporaryGraphDatabase createResource(ExtensionContext extensionContext) {
    try {
      TemporaryFolder temporaryFolder = new TemporaryFolder();
      GraphDatabaseService db = createDb(temporaryFolder.newFolder());
      return new TemporaryGraphDatabase(temporaryFolder, db);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    if (super.supportsParameter(parameterContext, extensionContext)) {
      return true;
    }

    return GraphDatabaseService.class.isAssignableFrom(parameterContext.getParameter().getType());
  }

  @Nonnull
  @Override
  protected Object convertResourceForParameter(@Nonnull Parameter parameter, @Nonnull TemporaryGraphDatabase resource) throws ParameterResolutionException, Exception {
    if (GraphDatabaseService.class.isAssignableFrom(parameter.getType())) {
      return resource.db;
    }

    throw new ParameterResolutionException("unable to resolve parameter for " + parameter);
  }

  @Override
  protected void cleanup(@Nonnull TemporaryGraphDatabase resource) {
    resource.db.shutdown();
    resource.temporaryFolder.delete();
  }

  @Nonnull
  public GraphDatabaseService createDb(@Nonnull File folder) {
    return new TestGraphDatabaseFactory().newImpermanentDatabase(folder);
  }

  public static class TemporaryGraphDatabase {

    @Nonnull
    private final TemporaryFolder temporaryFolder;
    @Nonnull
    private final GraphDatabaseService db;

    public TemporaryGraphDatabase(@Nonnull TemporaryFolder temporaryFolder, @Nonnull GraphDatabaseService db) {
      this.temporaryFolder = temporaryFolder;
      this.db = db;
    }

    @Nonnull
    public TemporaryFolder getTemporaryFolder() {
      return temporaryFolder;
    }

    @Nonnull
    public GraphDatabaseService getDb() {
      return db;
    }
  }

}
