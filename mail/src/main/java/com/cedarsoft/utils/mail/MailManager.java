package com.cedarsoft.utils.mail;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * <p/>
 * Date: 05.05.2004<br> Time: 20:59:09<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class MailManager {
  @NotNull
  private static final Category CAT = Logger.getLogger( MailManager.class );
  @NonNls
  private static final String MAIL_HOST = "mail.host";
  @NonNls
  private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";


  private Session session;

  private MailConfiguration configuration;

  public MailManager( @NotNull MailConfiguration mailConfiguration ) {
    this.configuration = mailConfiguration;
    session = getSession();
  }

  /**
   * Returns the session
   *
   * @return the session
   */
  @NotNull
  protected final Session getSession() {
    return Session.getInstance( getProperties(), getAuthenticator() );
  }

  @NotNull
  public Authenticator getAuthenticator() {
    return new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication( configuration.getSmtpUser(), configuration.getSmtpPass() );
      }
    };
  }

  /**
   * Erstellt eine Default-Nachricht mitentsprechendem Absender.
   *
   * @return a new mime message
   */
  @NotNull
  public MimeMessage createMessage() {
    MimeMessage message = new MimeMessage( session );
    try {
      InternetAddress from = new InternetAddress( configuration.getMailFrom() );
      from.setPersonal( configuration.getMailPersonal() );
      message.setFrom( from );
    } catch ( Exception e ) {
      CAT.error( "An error occured while creating message", e );
    }
    return message;
  }


  public static void sendMail( @NotNull Message message ) {
    new Thread( new MailWorker( message ) ).start();
  }

  @NotNull
  private Properties getProperties() {
    Properties properties = new Properties();
    properties.setProperty( MAIL_HOST, configuration.getMailHost() );
    properties.setProperty( MAIL_SMTP_AUTH, configuration.getMailSmtpAuth() );
    return properties;
  }

  /**
   * The mail worker
   */
  private static class MailWorker implements Runnable {
    private Message message;

    protected MailWorker( @NotNull Message message ) {
      this.message = message;
    }

    public void run() {
      try {
        Transport.send( message );
      } catch ( Exception e ) {
        CAT.error( "Error occured on message sending", e );
      }
    }
  }
}

