package com.cedarsoft.file.io;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import com.cedarsoft.file.BaseName;
import com.cedarsoft.file.Extension;
import com.cedarsoft.file.ExtensionSerializer;
import com.cedarsoft.file.FileName;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.serialization.stax.AbstractStaxMateSerializer;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * Serializer for file names
 */
public class FileNameSerializer extends AbstractStaxMateSerializer<FileName> {
  @NotNull
  @NonNls
  public static final String ELEMENT_EXTENSION = "extension";
  @NotNull
  @NonNls
  public static final String ELEMENT_BASE_NAME = "baseName";

  @NotNull
  private final ExtensionSerializer extensionSerializer;
  @NotNull
  private final BaseNameSerializer baseNameSerializer;

  public FileNameSerializer( @NotNull BaseNameSerializer baseNameSerializer, @NotNull ExtensionSerializer extensionSerializer ) {
    super( "fileName", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) );
    this.extensionSerializer = extensionSerializer;
    this.baseNameSerializer = baseNameSerializer;

    verifyDelegatingSerializerVersion( extensionSerializer, new Version( 1, 0, 0 ) );
    verifyDelegatingSerializerVersion( baseNameSerializer, new Version( 1, 0, 0 ) );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull FileName object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    baseNameSerializer.serialize( serializeTo.addElement( ELEMENT_BASE_NAME ), object.getBaseName(), context );
    extensionSerializer.serialize( serializeTo.addElement( ELEMENT_EXTENSION ), object.getExtension(), context );
    return serializeTo;
  }

  @NotNull
  @Override
  public FileName deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    nextTag( deserializeFrom, ELEMENT_BASE_NAME );
    BaseName baseName = baseNameSerializer.deserialize( deserializeFrom, context );

    nextTag( deserializeFrom, ELEMENT_EXTENSION );
    Extension extension = extensionSerializer.deserialize( deserializeFrom, context );

    closeTag( deserializeFrom );

    return new FileName( baseName, extension );
  }
}
