package com.cedarsoft.commons.javafx;

import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nonnull;

/**
 * @author Christian Erbelding (<a href="mailto:ce@cedarsoft.com">ce@cedarsoft.com</a>)
 */
public class EnumTranslatorUtil {
  /**
   * Holds the reference to the currently active translator that is set in a static field
   */
  @Nonnull
  private static final AtomicReference<EnumTranslator> ENUM_TRANSLATOR = new AtomicReference<>(item -> {
    System.err.println("No enum translator set in com.cedarsoft.commons.javafx.EnumListCell");
    return item.name();
  });

  private EnumTranslatorUtil() {
    // utility class
  }

  @Nonnull
  public static EnumTranslator getEnumTranslator() {
    return ENUM_TRANSLATOR.get();
  }

  public static void setEnumTranslator(@Nonnull EnumTranslator enumTranslator) {
    ENUM_TRANSLATOR.set(enumTranslator);
  }

}
