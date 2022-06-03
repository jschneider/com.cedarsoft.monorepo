/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.exceptions.handling;


import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.jetbrains.annotations.NotNull;

import com.cedarsoft.exceptions.ApplicationException;
import com.cedarsoft.exceptions.ErrorCode;
import com.cedarsoft.i18n.I18nConfiguration;

public class MyTestException extends ApplicationException {
  public MyTestException(@SuppressWarnings("TypeMayBeWeakened") @Nonnull TestExceptionDetails exceptionDetails,
                         @Nullable Map<String, ?> parameters) {
    super(exceptionDetails, parameters);
  }

  /**
   */
  @Immutable
  public enum TestExceptionDetails implements Details {
    ERROR_1(701),
    ERROR_2(702);

    @Nonnull
    private final ErrorCode errorCode;

    TestExceptionDetails(int errorCode) {
      this(ErrorCode.create("TD", errorCode));
    }

    TestExceptionDetails(@Nonnull ErrorCode errorCode) {
      this.errorCode = errorCode;
    }


    @Nonnull
    @Override
    public String getTitle(@Nonnull I18nConfiguration i18nConfiguration, @Nullable Map<String, ?> parameters) {
      return "da title";
    }

    @Nonnull
    @Override
    public ErrorCode getErrorCode() {
      return errorCode;
    }

    @NotNull
    @Override
    public String getLocalizedMessage(@Nonnull I18nConfiguration i18nConfiguration, @org.jetbrains.annotations.Nullable Map<String, ?> parameters) {
      return "A very long localized messages with a new line:\nThis is the next line!\nAnd another\n\nTwo new lines before";
    }
  }
}
