package it.neckar.open.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

/**
 * Marks variables / methods that are only valid within the context of a paint method
 */
@Documented
@Inherited
public @interface PaintContext {
}
