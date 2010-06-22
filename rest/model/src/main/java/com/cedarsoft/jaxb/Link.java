package com.cedarsoft.jaxb;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

/**
 *
 */
@XmlRootElement( namespace = Link.NAMESPACE_XLINK )
@XmlType( namespace = Link.NAMESPACE_XLINK )
@XmlAccessorType( XmlAccessType.FIELD )
public class Link extends AbstractJaxbObject {
  @NonNls
  @NotNull
  public static final String NAMESPACE_XLINK = "http://www.w3.org/1999/xlink";
  @NotNull
  @NonNls
  public static final String SELF = "self";
  @NotNull
  @NonNls
  public static final String ENCLOSURE = "enclosure";

  @NotNull
  @NonNls
  @XmlAttribute
  private String type;

  public Link() {
  }

  public Link( @NotNull URI href, @NotNull @NonNls String type ) {
    this.type = type;
    setHref( href );
  }

  @NotNull
  @NonNls
  public String getType() {
    return type;
  }
}
