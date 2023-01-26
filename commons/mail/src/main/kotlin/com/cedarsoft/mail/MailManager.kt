/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.cedarsoft.org/gpl3ce
 * (GPL 3 with Classpath Exception)
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
package com.cedarsoft.mail

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailManager(
  private val configuration: MailConfiguration,
) {

  private val session: Session = createSession()

  private fun createSession(): Session {
    return Session.getInstance(properties, authenticator)
  }

  val authenticator: Authenticator = object : Authenticator() {
    override fun getPasswordAuthentication(): PasswordAuthentication {
      return PasswordAuthentication(configuration.smtpUser, configuration.smtpPass)
    }
  }

  /**
   * Erstellt eine Default-Nachricht mitentsprechendem Absender.
   */
  fun createMessage(): MimeMessage {
    val message = MimeMessage(session)
    try {
      val from = InternetAddress(configuration.mailFrom)
      from.personal = configuration.mailPersonal
      message.setFrom(from)
    } catch (e: Exception) {
      logger.error("An error occured while creating message", e)
    }
    return message
  }

  private val properties: Properties
    get() {
      val properties = Properties()
      properties.setProperty(MAIL_HOST, configuration.mailHost)
      properties.setProperty(MAIL_SMTP_AUTH, configuration.mailSmtpAuth)
      return properties
    }

  /**
   * The mail worker
   */
  private class MailWorker(private val message: Message) : Runnable {
    override fun run() {
      try {
        Transport.send(message)
      } catch (e: Exception) {
        logger.error("Error occured on message sending", e)
      }
    }
  }

  companion object {
    val logger: Logger = LoggerFactory.getLogger(MailManager::class.java)

    private const val MAIL_HOST = "mail.host"
    private const val MAIL_SMTP_AUTH = "mail.smtp.auth"

    @JvmStatic
    @Deprecated("Attention: Spawns a new thread!")
    fun sendMail(message: Message) {
      Thread(MailWorker(message)).start()
    }
  }
}
