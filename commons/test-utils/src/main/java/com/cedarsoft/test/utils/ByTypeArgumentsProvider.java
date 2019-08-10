package com.cedarsoft.test.utils;

import static org.junit.jupiter.params.provider.Arguments.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.support.*;

/**
 * Extracts test data from fields
 *
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ByTypeArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<ByTypeSource> {

  private Class<?> type;

  @Override
  public void accept(ByTypeSource annotation) {
    this.type = annotation.type();
  }

  @Override
  public Stream<Arguments> provideArguments(@Nonnull ExtensionContext context) {
    List<Arguments> fieldsValues = getFieldsValues(context);
    List<Arguments> methodValues = getMethodValues(context);

    if (fieldsValues.isEmpty() && methodValues.isEmpty()) {
      throw new IllegalStateException("No data fields/methods found in " + context.getRequiredTestClass().getName());
    }

    return Stream.concat(fieldsValues.stream(), methodValues.stream());
  }

  @Nonnull
  private List<Arguments> getFieldsValues(@Nonnull ExtensionContext context) {
    List<Arguments> entries = new ArrayList<>();

    for (Field declaredField : context.getRequiredTestClass().getDeclaredFields()) {
      if (declaredField.getType().equals(this.type)) {

        //Warnings when marked as DataPoint
        for (Annotation declaredAnnotation : declaredField.getDeclaredAnnotations()) {
          if (declaredAnnotation.annotationType().getName().equals("org.junit.experimental.theories.DataPoint")) {
            System.err.println("Remove @DataPoint annotation at " + getClass().getName() + "." + declaredField.getName());
          }
        }

        try {
          Object value = getValue(declaredField, context.getTestInstance().orElse(null));
          entries.add(toArguments(value));
        }
        catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    return entries;
  }

  @Nonnull
  private static Object getValue(@Nonnull Field declaredField, @Nullable Object testInstance) throws IllegalAccessException {
    //Warning when not static!
    if (Modifier.isStatic(declaredField.getModifiers())) {
      return declaredField.get(null);
    }

    if (testInstance == null) {
      throw new IllegalStateException("Test instance requried");
    }

    return declaredField.get(testInstance);
  }

  @Nonnull
  private List<Arguments> getMethodValues(@Nonnull ExtensionContext context) {
    List<Arguments> entries = new ArrayList<>();

    for (Method declaredMethod : context.getRequiredTestClass().getDeclaredMethods()) {
      if (declaredMethod.isSynthetic()) {
        continue;
      }

      if (declaredMethod.getReturnType().equals(this.type)) {

        //Warnings when marked as DataPoint
        for (Annotation declaredAnnotation : declaredMethod.getDeclaredAnnotations()) {
          if (declaredAnnotation.annotationType().getName().equals("org.junit.experimental.theories.DataPoint")) {
            System.err.println("Remove @DataPoint annotation at " + getClass().getName() + "." + declaredMethod.getName());
          }
        }

        try {
          Object value = getValue(declaredMethod, context.getTestInstance().orElse(null));
          entries.add(toArguments(value));
        }
        catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }

    return entries;
  }

  @Nonnull
  private static Object getValue(@Nonnull Method declaredMethod, @Nullable Object testInstance) throws IllegalAccessException, InvocationTargetException {
    //Warning when not static!
    if (Modifier.isStatic(declaredMethod.getModifiers())) {
      return declaredMethod.invoke(null);
    }

    if (testInstance == null) {
      throw new IllegalStateException("Test instance requried");
    }

    return declaredMethod.invoke(testInstance);
  }

  @Nonnull
  private static Arguments toArguments(@Nonnull Object item) {
    if (item instanceof Arguments) {
      return (Arguments) item;
    }
    if (item instanceof Object[]) {
      return arguments((Object[]) item);
    }
    return arguments(item);
  }
}
