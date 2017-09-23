package com.cedarsoft.serialization.neo4j.test.utils;

import com.cedarsoft.test.utils.TemporaryFolderExtension;
import org.junit.jupiter.api.extension.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a test as neo4j base
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(TemporaryFolderExtension.class)
public @interface WithNeo4jDb {
}
