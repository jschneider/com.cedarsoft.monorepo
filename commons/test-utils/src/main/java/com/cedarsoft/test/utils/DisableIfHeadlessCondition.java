package com.cedarsoft.test.utils;

import org.junit.jupiter.api.extension.*;

import javax.annotation.Nonnull;
import java.awt.GraphicsEnvironment;
import java.util.Optional;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.*;
import static org.junit.platform.commons.util.AnnotationUtils.*;

/**
 * Disables tests when running headless
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class DisableIfHeadlessCondition implements ExecutionCondition {
  @Nonnull
  private static final ConditionEvaluationResult ENABLED_BY_DEFAULT = enabled("@DisableWhenHeadless is not present");
  @Nonnull
  static final ConditionEvaluationResult DISABLED_HEADLESS = disabled("Disabled because running headless");
  @Nonnull
  static final ConditionEvaluationResult ENABLED_NOT_HEADLESS = enabled("Enabled - *not* running headless");

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(@Nonnull ExtensionContext context) {
    Optional<DisableIfHeadless> optional = findAnnotation(context.getElement(), DisableIfHeadless.class);
    if (optional.isPresent()) {
      if (GraphicsEnvironment.isHeadless()) {
        return DISABLED_HEADLESS;
      }

      return ENABLED_NOT_HEADLESS;
    }
    return ENABLED_BY_DEFAULT;
  }
}
