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
package it.neckar.open.crypt

import it.neckar.open.resources.getResourceSafe
import org.apache.commons.codec.binary.Base64
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.KeyFactory
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher

/**
 */
class CertTest {
  @Test
  @Throws(IOException::class, GeneralSecurityException::class)
  fun testSupport() {
    val support = X509Support(javaClass.getResourceSafe("/test.crt"), javaClass.getResourceSafe("/test.der"))
    Assertions.assertEquals(SCRAMBLED, String(Base64.encodeBase64(support.cipher(PLAINTEXT.toByteArray(StandardCharsets.UTF_8))), StandardCharsets.UTF_8))
    Assertions.assertEquals(PLAINTEXT, String(support.decipher(Base64.decodeBase64(SCRAMBLED.toByteArray(StandardCharsets.UTF_8))), StandardCharsets.UTF_8))
  }

  @Test
  @Throws(Exception::class)
  fun testSign() {
    val support = X509Support(javaClass.getResourceSafe("/test.crt"), javaClass.getResourceSafe("/test.der"))
    Assertions.assertFalse(support.verifySignature(PLAINTEXT.toByteArray(StandardCharsets.UTF_8), Signature(SCRAMBLED.substring(0, 128).toByteArray(StandardCharsets.UTF_8))))
    val signature = support.sign(PLAINTEXT.toByteArray(StandardCharsets.UTF_8))
    Assertions.assertNotNull(signature)
    Assertions.assertTrue(support.verifySignature(PLAINTEXT.toByteArray(StandardCharsets.UTF_8), signature))
    val support2 = X509Support(javaClass.getResourceSafe("/test.crt"))
    Assertions.assertTrue(support2.verifySignature(PLAINTEXT.toByteArray(StandardCharsets.UTF_8), signature))
  }

  @Test
  @Throws(Exception::class)
  fun testCert() {
    val inStream = DataInputStream(javaClass.getResourceSafe("/test.crt").openStream())
    val cf = CertificateFactory.getInstance("X.509")
    val cert = cf.generateCertificate(inStream) as X509Certificate
    inStream.close()
    Assertions.assertNotNull(cert)
    cert.checkValidity()
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, cert)
    val clear = cipher.doFinal(Base64.decodeBase64(SCRAMBLED.toByteArray(StandardCharsets.UTF_8)))
    Assertions.assertEquals(PLAINTEXT, String(clear, StandardCharsets.UTF_8))
  }

  @Test
  @Throws(Exception::class)
  fun testKey() {
    val inStream: InputStream = DataInputStream(javaClass.getResourceSafe("/test.der").openStream())
    val keyBytes = ByteArray(inStream.available())
    inStream.read(keyBytes)
    inStream.close()
    val keyFactory = KeyFactory.getInstance("RSA")

    // decipher private key
    val privSpec = PKCS8EncodedKeySpec(keyBytes)
    val privKey = keyFactory.generatePrivate(privSpec) as RSAPrivateKey
    Assertions.assertNotNull(privKey)
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, privKey)
    val bytes = cipher.doFinal(PLAINTEXT.toByteArray(StandardCharsets.UTF_8))
    Assertions.assertEquals(SCRAMBLED, String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8))
  }

  companion object {
    private const val SCRAMBLED = "c9iNeeyxe/Q6rU5+F+be3hvdok3/p6kYZwsmzQe6x+6jSjr8o3wWtrOQBAy090npe6So2hRJMHfrXkYkX/fL+3pQMLgqvqvmbaIce8uQmgEkjaDMe3BFyT9xiOYrg7g1OgYeJSWDTN9V4i2os3dD+r+9ryw8uwTEIECE40e0FXs="
    private const val PLAINTEXT = "Klartext"
  }
}
