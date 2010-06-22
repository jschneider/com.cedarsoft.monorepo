package com.cedarsoft.rest;

import com.cedarsoft.jaxb.Link;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
public class LinkTest extends AbstractJaxbTest<Link> {
  @NotNull
  @Override
  protected Class<Link> getJaxbType() {
    return Link.class;
  }

  @NotNull
  @Override
  protected String expectedXml() {
    return "<link xmlns=\"http://www.w3.org/1999/xlink\" href=\"http://www.test.de/asdf\" type=\"self\" />";
  }

  @NotNull
  @Override
  protected Link createObjectToSerialize() throws URISyntaxException {
    return new Link( new URI( "http://www.test.de/asdf" ), Link.SELF );
  }
}
