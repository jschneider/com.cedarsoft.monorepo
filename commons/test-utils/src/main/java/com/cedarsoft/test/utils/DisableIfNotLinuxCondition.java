package com.cedarsoft.test.utils;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.*;
import static org.junit.platform.commons.util.AnnotationUtils.*;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.*;

/**
 * Tests are disabled on non linux OS (windows)
 *
 */
public class DisableIfNotLinuxCondition implements ExecutionCondition {
  @Nonnull
  private static final ConditionEvaluationResult ENABLED_BY_DEFAULT = enabled("@OnlyLinux is not present");
  @Nonnull
  static final ConditionEvaluationResult DISABLED_NON_LINUX = disabled("Disabled because running on other OS than Linux");
  @Nonnull
  static final ConditionEvaluationResult ENABLED_LINUX = enabled("Enabled - running on Linux");

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(@Nonnull ExtensionContext context) {
    Optional<OnlyLinux> optional = findAnnotation(context.getElement(), OnlyLinux.class);
    if (optional.isPresent()) {
      if (OS.LINUX.isCurrentOs()) {
        return ENABLED_LINUX;
      }
      else {
        return DISABLED_NON_LINUX;
      }
    }

    return ENABLED_BY_DEFAULT;
  }
}
