package com.cedarsoft.file.io;

import com.cedarsoft.Version;
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
  public static final String ELEMENT_DELIMITER = "delimiter";
  @NotNull
  @NonNls
  public static final String ELEMENT_BASE_NAME = "baseName";

  public FileNameSerializer() {
    super( "fileName", new Version( 1, 0, 0 ) );
  }

  @NotNull
  @Override
  public SMOutputElement serialize( @NotNull SMOutputElement serializeTo, @NotNull FileName object, @NotNull Lookup context ) throws IOException, XMLStreamException {
    serializeTo.addElement( ELEMENT_BASE_NAME ).addCharacters( object.getBaseName().getName() );
    serializeTo.addElement( ELEMENT_DELIMITER ).addCharacters( object.getDelimiterNonNull() );
    serializeTo.addElement( ELEMENT_EXTENSION ).addCharacters( object.getExtensionNonNull() );
    return serializeTo;
  }

  @NotNull
  @Override
  public FileName deserialize( @NotNull XMLStreamReader2 deserializeFrom, @NotNull Lookup context ) throws IOException, XMLStreamException {
    final String[] delimiter = {null};
    final String[] base = {null};
    final String[] extension = {null};

    visitChildren( deserializeFrom, new CB() {
      @Override
      public void tagEntered( @NotNull XMLStreamReader2 deserializeFrom, @NotNull String tagName ) throws XMLStreamException, IOException {
        if ( tagName.equals( ELEMENT_DELIMITER ) ) {
          delimiter[0] = getText( deserializeFrom );
        }
        if ( tagName.equals( ELEMENT_BASE_NAME ) ) {
          base[0] = getText( deserializeFrom );
        }
        if ( tagName.equals( ELEMENT_EXTENSION ) ) {
          extension[0] = getText( deserializeFrom );
        }
      }
    } );

    if ( delimiter[0] == null ) {
      return new FileName( base[0], extension[0] );
    } else {
      return new FileName( base[0], delimiter[0], extension[0] );
    }
  }
}
