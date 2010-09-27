package com.cedarsoft.quantity;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Documented
@Inherited

@Quantity
public @interface Length {

  class Type implements Quantity.Type<Type> {
    @NotNull
    public static final Type INSTANCE = new Type();

    @NotNull
    public static Type get() {
      return INSTANCE;
    }

    private Type() {
    }
  }
}
