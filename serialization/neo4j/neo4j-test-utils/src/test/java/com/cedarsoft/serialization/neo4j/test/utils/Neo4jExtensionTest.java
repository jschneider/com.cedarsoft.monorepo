package com.cedarsoft.serialization.neo4j.test.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.neo4j.graphdb.GraphDatabaseService;

import javax.annotation.Nonnull;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@WithNeo4j
public class Neo4jExtensionTest {
  @Test
  void testDb(@Nonnull GraphDatabaseService graphDatabaseService) {
    Assertions.assertThat(graphDatabaseService).isNotNull();
  }
}