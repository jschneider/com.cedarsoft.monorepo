package it.neckar.open.serialization.neo4j.test.utils

import org.junit.jupiter.api.extension.ExtendWith

/**
 * Tests annotated with this annotation may get a parameter [org.neo4j.graphdb.GraphDatabaseService]
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(Neo4jExtension::class)
annotation class WithNeo4j
