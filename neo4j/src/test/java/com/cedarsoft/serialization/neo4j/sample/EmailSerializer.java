package com.cedarsoft.serialization.neo4j.sample;

import com.cedarsoft.serialization.neo4j.AbstractNeo4jSerializer;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import org.neo4j.graphdb.Node;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
* @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
*/
public class EmailSerializer extends AbstractNeo4jSerializer<Email> {
  public EmailSerializer() {
    super( "com.cedarsoft.test.email", VersionRange.single( 1, 0, 0 ) );
  }

  @Override
  public void serialize( @Nonnull Node serializeTo, @Nonnull Email object, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
    super.serialize( serializeTo, object, formatVersion );
    serializeTo.setProperty( "mail", object.getMail() );
  }

  @Nonnull
  @Override
  public Email deserialize( @Nonnull Node deserializeFrom, @Nonnull Version formatVersion ) throws IOException, VersionException, IOException {
    String mail = ( String ) deserializeFrom.getProperty( "mail" );
    return new Email( mail );
  }
}
