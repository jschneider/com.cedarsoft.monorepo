package com.cedarsoft.inject;

import com.google.inject.util.Types;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.util.Collection;

/**
 *
 */
public class GuiceHelperTest {
  @Test
  public void testIt() {
    assertEquals( GuiceHelper.superCollectionOf( String.class ).toString(), "java.util.Collection<? extends java.lang.String>" );
    assertEquals( GuiceHelper.superListOf( String.class ).toString(), "java.util.List<? extends java.lang.String>" );
  }

  @Test
  public void testGuice() {
    assertEquals( Types.listOf( String.class ).toString(), "java.util.List<java.lang.String>" );
    assertEquals( Types.listOf( Types.subtypeOf( String.class ) ).toString(), "java.util.List<? extends java.lang.String>" );
    assertEquals( Types.newParameterizedType( Collection.class, Types.subtypeOf( String.class ) ).toString(), "java.util.Collection<? extends java.lang.String>" );
  }
}
