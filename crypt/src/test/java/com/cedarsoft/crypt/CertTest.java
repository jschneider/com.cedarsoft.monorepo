package com.cedarsoft.crypt;

import org.apache.commons.codec.binary.Base64;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * <p/>
 * Date: Jul 20, 2007<br>
 * Time: 11:53:12 PM<br>
 */
public class CertTest {
  private static final String SCRAMBLED = "c9iNeeyxe/Q6rU5+F+be3hvdok3/p6kYZwsmzQe6x+6jSjr8o3wWtrOQBAy090npe6So2hRJMHfrXkYkX/fL+3pQMLgqvqvmbaIce8uQmgEkjaDMe3BFyT9xiOYrg7g1OgYeJSWDTN9V4i2os3dD+r+9ryw8uwTEIECE40e0FXs=";
  private static final String PLAINTEXT = "Klartext";

  @Test
  public void testSupport() throws IOException, GeneralSecurityException {
    X509Support support = new X509Support( getClass().getResource( "/test.crt" ), getClass().getResource( "/test.der" ) );
    assertEquals( SCRAMBLED, new String( Base64.encodeBase64( support.cipher( PLAINTEXT.getBytes() ) ) ) );
    assertEquals( PLAINTEXT, new String( support.decipher( Base64.decodeBase64( SCRAMBLED.getBytes() ) ) ) );
  }

  @Test
  public void testSign() throws Exception {
    X509Support support = new X509Support( getClass().getResource( "/test.crt" ), getClass().getResource( "/test.der" ) );

    assertFalse( support.verifySignature( PLAINTEXT.getBytes(), new Signature( SCRAMBLED.getBytes() ) ) );
    assertFalse( support.verifySignature( PLAINTEXT.getBytes(), new Signature( PLAINTEXT.getBytes() ) ) );

    Signature signature = support.sign( PLAINTEXT.getBytes() );
    assertNotNull( signature );
    assertTrue( support.verifySignature( PLAINTEXT.getBytes(), signature ) );


    X509Support support2 = new X509Support( getClass().getResource( "/test.crt" ) );
    assertTrue( support2.verifySignature( PLAINTEXT.getBytes(), signature ) );
  }

  @Test
  public void testCert() throws Exception {
    DataInputStream inStream = new DataInputStream( getClass().getResource( "/test.crt" ).openStream() );

    CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
    X509Certificate cert = ( X509Certificate ) cf.generateCertificate( inStream );
    inStream.close();
    assertNotNull( cert );

    cert.checkValidity();

    Cipher cipher = Cipher.getInstance( "RSA" );
    cipher.init( Cipher.DECRYPT_MODE, cert );

    byte[] clear = cipher.doFinal( Base64.decodeBase64( SCRAMBLED.getBytes() ) );
    assertEquals( PLAINTEXT, new String( clear ) );
  }

  @Test
  public void testKey() throws Exception {
    InputStream inStream = new DataInputStream( getClass().getResource( "/test.der" ).openStream() );
    byte[] keyBytes = new byte[inStream.available()];
    inStream.read( keyBytes );
    inStream.close();

    KeyFactory keyFactory = KeyFactory.getInstance( "RSA" );

    // decipher private key
    PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec( keyBytes );
    RSAPrivateKey privKey = ( RSAPrivateKey ) keyFactory.generatePrivate( privSpec );
    assertNotNull( privKey );

    Cipher cipher = Cipher.getInstance( "RSA" );
    cipher.init( Cipher.ENCRYPT_MODE, privKey );

    byte[] bytes = cipher.doFinal( PLAINTEXT.getBytes() );
    assertEquals( SCRAMBLED, new String( Base64.encodeBase64( bytes ) ) );
  }
}

