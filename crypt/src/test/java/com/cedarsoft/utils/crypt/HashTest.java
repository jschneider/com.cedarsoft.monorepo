package com.cedarsoft.utils.crypt;

import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * <p/>
 * Date: Jul 20, 2007<br>
 * Time: 11:28:04 PM<br>
 */
public class HashTest {
  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    Hash hash = Hash.fromHex( Algorithm.SHA256, "1234" );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    new ObjectOutputStream( out ).writeObject( hash );

    Hash deserialized = ( Hash ) new ObjectInputStream( new ByteArrayInputStream( out.toByteArray() ) ).readObject();
    assertEquals( deserialized, hash );
  }

  @Test
  public void testIt() throws NoSuchAlgorithmException, IOException {
    URL paris = getClass().getResource( "/paris.jpg" );
    assertNotNull( paris );

    ResourceHashCalculator calculator = new ResourceHashCalculator();
    assertEquals( "fbd5f9b6c0fd2035c490e46be0bc3ec3", calculator.calculate( Algorithm.MD5, paris ).getValueAsHex() );//value read using md5sum cmd line tool
    assertEquals( "aa5371938c4190543bddcfc1193a247717feba06", calculator.calculate( Algorithm.SHA1, paris ).getValueAsHex() );//value read using sha1sum cmd line tool
  }

  @Test
  public void testRound() {
    Hash hash = new Hash( Algorithm.SHA256, "asdf".getBytes() );

    assertEquals( hash.getValueAsHex(), "61736466" );
    assertEquals( Hash.fromHex( hash.getAlgorithm(), hash.getValueAsHex() ), hash );
  }
  
  @Test
  public void testAlgos() throws IOException {
    URL paris = getClass().getResource( "/paris.jpg" );
    assertNotNull( paris );

    ResourceHashCalculator calculator = new ResourceHashCalculator();

    for ( Algorithm algorithm : Algorithm.values() ) {
      String value = calculator.calculate( algorithm, paris ).getValueAsHex();
      assertNotNull( value );
      assertTrue( value.length() > 10 );
    }
  }
}
