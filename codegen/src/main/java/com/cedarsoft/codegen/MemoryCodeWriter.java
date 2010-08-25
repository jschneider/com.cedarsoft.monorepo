package com.cedarsoft.codegen;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
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
  public byte[] getFileContent( @NotNull @NonNls String packageName, @NotNull @NonNls String fileName ) {
    String fqnName = packageName + "." + fileName;
    ByteArrayOutputStream found = files.get( fqnName );
    if ( found == null ) {
      throw new IllegalArgumentException( "No file found for <" + fqnName + ">" );
    }

    return found.toByteArray();
  }

  @Override
  public void close() throws IOException {
  }
}
