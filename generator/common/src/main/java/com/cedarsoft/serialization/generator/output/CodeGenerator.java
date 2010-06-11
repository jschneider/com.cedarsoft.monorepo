package com.cedarsoft.serialization.generator.output;

import com.cedarsoft.serialization.generator.decision.DecisionCallback;
import com.sun.codemodel.JCodeModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @param <T> the type of the decision callback
 */
public class CodeGenerator<T extends DecisionCallback> {
  @NotNull
  protected final JCodeModel model;
  @NotNull
  private final ParseExpressionFactory parseExpressionFactory;

  @NotNull
  private final T decisionCallback;

  @NotNull
  private final List<MethodDecorator> methodDecorators = new ArrayList<MethodDecorator>();

  public CodeGenerator( @NotNull T decisionCallback ) {
    this( new JCodeModel(), decisionCallback );
  }

  protected CodeGenerator( @NotNull JCodeModel model, @NotNull T decisionCallback ) {
    this.model = model;
    this.parseExpressionFactory = new ParseExpressionFactory( model );
    this.decisionCallback = decisionCallback;
  }

  @NotNull
  public ParseExpressionFactory getParseExpressionFactory() {
    return parseExpressionFactory;
  }

  @NotNull
  public JCodeModel getModel() {
    return model;
  }

  @NotNull
  public T getDecisionCallback() {
    return decisionCallback;
  }

  public void addMethodDecorator( @NotNull MethodDecorator decorator ) {
    this.methodDecorators.add( decorator );
  }

  @NotNull
  public List<? extends MethodDecorator> getMethodDecorators() {
    return Collections.unmodifiableList( methodDecorators );
  }
}
