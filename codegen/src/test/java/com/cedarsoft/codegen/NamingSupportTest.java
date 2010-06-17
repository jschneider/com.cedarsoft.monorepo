package com.cedarsoft.codegen;

import org.testng.annotations.*;

import static org.testng.Assert.*;

/**
 *
 */
public class NamingSupportTest {
  @BeforeMethod
  protected void setUp() throws Exception {
  }

  @Test
  public void testIt() {
    assertEquals( NamingSupport.createXmlElementName( "String" ), "string" );
    assertEquals( NamingSupport.createXmlElementName( "ACamelCase" ), "acamelcase" );
  }
}
