package com.cedarsoft.commons.struct;

import org.jetbrains.annotations.NotNull;

/**
 * Validates a path
 */
public interface PathValidator {
  /**
   * Validate the path
   *
   * @param path the path
   * @throws ValidationFailedException
   */
  void validate( @NotNull Path path ) throws ValidationFailedException;
}