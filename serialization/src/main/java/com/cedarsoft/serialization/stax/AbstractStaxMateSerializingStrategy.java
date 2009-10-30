package com.cedarsoft.serialization.stax;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.serialization.jdom.JDomSerializingStrategy;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * @param <T> the type
 */
public abstract class AbstractStaxMateSerializingStrategy<T> extends AbstractStaxMateSerializer<T> implements StaxMateSerializingStrategy<T> {
  @NotNull
  @NonNls
  private final String id;

  @NotNull
  private final Class<? extends T> supportedType;

  protected AbstractStaxMateSerializingStrategy( @NotNull String id, @NotNull Class<? extends T> supportedType ) {
    super( id );
    this.id = id;
    this.supportedType = supportedType;
  }

  @NotNull
  public String getId() {
    return id;
  }

  public boolean supports( @NotNull Object object ) {
    return supportedType.isAssignableFrom( object.getClass() );
  }

  @NotNull
  public SMOutputElement serialize( @NotNull SMOutputElement element, @NotNull T object ) throws IOException, XMLStreamException {
    return serialize( element, object, Lookups.emtyLookup() );
  }

  @NotNull
  public T deserialize( @NotNull @NonNls XMLStreamReader2 reader ) throws IOException, XMLStreamException {
    return deserialize( reader, Lookups.emtyLookup() );
  }
}
