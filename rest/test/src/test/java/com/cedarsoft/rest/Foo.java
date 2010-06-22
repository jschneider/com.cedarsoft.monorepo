package com.cedarsoft.rest;

import com.cedarsoft.jaxb.AbstractJaxbObject;

import javax.xml.bind.annotation.XmlRootElement;

/**
*
*/
@XmlRootElement( namespace = "test:foo" )
public class Foo extends AbstractJaxbObject {
  private String daValue = "default";

  public String getDaValue() {
    return daValue;
  }

  public void setDaValue( String daValue ) {
    this.daValue = daValue;
  }
}
