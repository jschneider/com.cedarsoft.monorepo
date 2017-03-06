package com.cedarsoft.exceptions.handling;

import com.cedarsoft.exceptions.ApplicationException;
import com.cedarsoft.exceptions.ErrorCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public class MyTestException extends ApplicationException {
  @Nonnull
  public static final ErrorCode.Prefix PREFIX = new ErrorCode.Prefix("TD");

  public MyTestException(@Nonnull TestExceptionDetails exceptionDetails, @Nonnull Object... messageArguments) {
    super(exceptionDetails, messageArguments);
  }

  /**
   * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
   */
  public static enum TestExceptionDetails implements Details {
    ERROR_1(701),
    ERROR_2(702);
    public static final String CATEGORY_TITLE = "title";

    @Nonnull
    private final ErrorCode errorCode;

    TestExceptionDetails(int errorCode) {
      this(new ErrorCode(PREFIX, errorCode));
    }

    TestExceptionDetails(@Nonnull ErrorCode errorCode) {
      this.errorCode = errorCode;
    }

    @Nonnull
    @Override
    public String getLocalizedMessage(@Nullable Object... messageArguments) {
      return "localized message";
    }

    @Nonnull
    @Override
    public String getLocalizedMessage(@Nonnull Locale locale, @Nullable Object... messageArguments) {
      return "localized message";
    }

    @Nonnull
    @Override
    public String getTitle(@Nullable Object... messageArguments) {
      return "da title";
    }

    @Nonnull
    @Override
    public String getTitle(@Nonnull Locale locale, @Nullable Object... messageArguments) {
      return "da title";
    }

    @Nonnull
    @Override
    public ErrorCode getErrorCode() {
      return errorCode;
    }
  }
}
