package com.cedarsoft.codegen;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MemoryCodeWriter extends CodeWriter {
  @NotNull
  private final Map<String, ByteArrayOutputStream> files = new HashMap<String, ByteArrayOutputStream>();

  @Override
  public OutputStream openBinary( JPackage pkg, String fileName ) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    files.put( pkg.name() + "." + fileName, out );
    return out;
  }

  @NotNull
  public Map<String, ByteArrayOutputStream> getFiles() {
    return Collections.unmodifiableMap( files );
  }

  @NotNull
  public String getFileContent( @NotNull @NonNls String packageName, @NotNull @NonNls String fileName ) {
    String fqnName = packageName + "." + fileName;
    ByteArrayOutputStream found = files.get( fqnName );
    if ( found == null ) {
      throw new IllegalArgumentException( "No file found for <" + fqnName + ">" );
    }

    return new String( found.toByteArray() );
  }

  @Override
  public void close() throws IOException {
  }

  @NotNull
  @NonNls
  public String allFilesToString() {
    List<String> sortedKeys = new ArrayList<String>( files.keySet() );
    Collections.sort( sortedKeys );

    StringBuilder large = new StringBuilder();
    for ( String key : sortedKeys ) {
      large.append( "-----------------------------------" ).append( key ).append( "-----------------------------------\n" );
      large.append( files.get( key ) );
    }

    return large.toString();
  }
}
