package com.cedarsoft.rest.generator.test.jaxb;

import com.cedarsoft.rest.AbstractJaxbTest;

import java.util.Arrays;
import java.util.HashSet;

public class BarModelTest
  extends AbstractJaxbTest {


  @Override
  protected Class<BarModel> getJaxbType() {
    return BarModel.class;
  }

  @Override
  protected String expectedXml() {
    return "<ns2:barModel xmlns:ns2=\"http://www.cedarsoft.com/rest/generator/test/BarModel\">\n" +
      "  <daInt>42</daInt>\n" +
      "  <daString>daString</daString>\n" +
      "  <set>set</set>\n" +
      "  <stringList>stringList</stringList>\n" +
      "  <wildStringList>wildStringList</wildStringList>\n" +
      "</ns2:barModel>";
  }

  @Override
  protected BarModel createObjectToSerialize()
    throws Exception {
    BarModel object = new BarModel();
    object.setDaInt( 42 );
    object.setDaString( "daString" );
    object.setStringList( Arrays.asList( "stringList", "4" ) );
    object.setWildStringList( Arrays.asList( "wildStringList", "2", "3" ) );
    object.setSet( new HashSet( Arrays.asList( "set", "other" ) ) );
    return object;
  }

}
