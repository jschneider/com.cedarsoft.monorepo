package com.cedarsoft.serialization.stax;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.SMOutputFactory;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/**
 * Helper class for stax
 */
public class StaxSupport {
  private StaxSupport() {
  }

  @NotNull
  private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();
  @NotNull
  private static final XMLOutputFactory XML_OUTPUT_FACTORY = XMLOutputFactory.newInstance();
  @NotNull
  private static final SMInputFactory SM_INPUT_FACTORY = new SMInputFactory( XML_INPUT_FACTORY );
  @NotNull
  private static final SMOutputFactory SM_OUTPUT_FACTORY = new SMOutputFactory( XML_OUTPUT_FACTORY );

  @NotNull
  public static SMOutputFactory getSmOutputFactory() {
    return SM_OUTPUT_FACTORY;
  }

  public static SMInputFactory getSmInputFactory() {
    return SM_INPUT_FACTORY;
  }

  @NotNull
  public static XMLOutputFactory getXmlOutputFactory() {
    return XML_OUTPUT_FACTORY;
  }

  @NotNull
  public static XMLInputFactory getXmlInputFactory() {
    return XML_INPUT_FACTORY;
  }
}
