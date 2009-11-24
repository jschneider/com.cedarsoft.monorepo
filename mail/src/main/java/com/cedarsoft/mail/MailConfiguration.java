package com.cedarsoft.mail;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * <p/>
 * Date: 21.06.2006<br>
 * Time: 23:19:52<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class MailConfiguration {
  @NotNull
  @NonNls
  private final String mailFrom;
  @NotNull
  @NonNls
  private final String mailHost;

  @NotNull
  @NonNls
  private final String smtpUser;
  @NotNull
  @NonNls
  private final String smtpPass;

  @NotNull
  @NonNls
  private final String mailPersonal;
  @NotNull
  @NonNls
  private final String mailSmtpAuth;


  public MailConfiguration( @NonNls @NotNull String mailHost, @NonNls @NotNull String mailFrom, @NonNls @NotNull String mailPersonal, @NonNls @NotNull String smtpUser, @NonNls @NotNull String smtpPass, @NonNls @NotNull String mailSmtpAuth ) {
    this.mailHost = mailHost;
    this.mailFrom = mailFrom;
    this.mailPersonal = mailPersonal;
    this.smtpUser = smtpUser;
    this.smtpPass = smtpPass;
    this.mailSmtpAuth = mailSmtpAuth;
  }

  @NonNls
  @NotNull
  public String getMailFrom() {
    return mailFrom;
  }

  @NonNls
  @NotNull
  public String getMailHost() {
    return mailHost;
  }

  @NonNls
  @NotNull
  public String getSmtpUser() {
    return smtpUser;
  }

  @NonNls
  @NotNull
  public String getSmtpPass() {
    return smtpPass;
  }

  @NonNls
  @NotNull
  public String getMailPersonal() {
    return mailPersonal;
  }

  @NonNls
  @NotNull
  public String getMailSmtpAuth() {
    return mailSmtpAuth;
  }
}
