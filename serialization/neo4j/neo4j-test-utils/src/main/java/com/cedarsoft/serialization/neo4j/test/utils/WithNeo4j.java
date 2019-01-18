package com.cedarsoft.serialization.neo4j.test.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.*;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Tests annotated with this annotation may get a paramter {@link GraphDatabaseService}
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(Neo4jExtension.class)
public @interface WithNeo4j {


}
