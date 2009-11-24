package com.cedarsoft.xml;

import org.jdom.Document;
import org.jdom.Element;
import org.testng.annotations.*;

/**
 */
public class XpathTest {
  private Element root;
  private Document doc;

  @BeforeMethod
  protected void setUp() throws Exception {
    root = new Element( "root" );
    doc = new Document( root );

    root.addContent( new Element( "child" ).addContent( new Element( "a" ).setText( "_0" ) ) );
    root.addContent( new Element( "child" ).addContent( new Element( "b" ).setText( "_1" ) ) );
    root.addContent( new Element( "child" ).setText( "_2" ) );
    root.addContent( new Element( "child" ).setText( "_3" ) );
  }

  @Test
  public void testDummy() {

  }

  //  public void testIt() throws JDOMException {
  //    XPath path = XPath.newInstance( "/*" );
  //    assertEquals( "/*", path.getXPath() );
  //    Element queried = ( Element ) path.selectSingleNode( doc );
  //    assertSame( root, queried );
  //  }
  //
  //  public void testMore() throws JDOMException {
  //    Element queried = ( Element ) XPath.newInstance( "/root/child[1]/a" ).selectSingleNode( doc );
  //    assertNotNull( queried );
  //    assertEquals( "a", queried.getName() );
  //    assertEquals( "_0", queried.getText() );
  //    assertEquals( 0, queried.getChildren().size() );
  //  }
  //
  //  public void testMore2() throws JDOMException {
  //    Element queried = ( Element ) XPath.newInstance( "/root/child[2]/b[1]" ).selectSingleNode( doc );
  //    assertNotNull( queried );
  //    assertEquals( "b", queried.getName() );
  //    assertEquals( "_1", queried.getText() );
  //    assertEquals( 0, queried.getChildren().size() );
  //  }
  //
  //  public void testPath() throws JDOMException {
  //    String xpath = "/root/child[2]/b";
  //    Element queried = ( Element ) XPath.newInstance( xpath ).selectSingleNode( doc );
  //    assertNotNull( queried );
  //    assertEquals( xpath, XPathCreator.createAbsolutePath( queried ) );
  //  }
}

