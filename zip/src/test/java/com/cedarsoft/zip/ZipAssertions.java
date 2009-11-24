package com.cedarsoft.zip;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Assertions for zip files
 */
public class ZipAssertions {
  private ZipAssertions() {
  }

  public static void compare( @NotNull ZipFile expected, @NotNull ZipFile actual ) {
    if ( expected == actual ) {
      return;
    }

    Set<String> testedActualEntries = new HashSet<String>( actual.size() );

    //Forward
    Enumeration<? extends ZipEntry> expectedEntries = expected.entries();
    while ( expectedEntries.hasMoreElements() ) {
      ZipEntry expectedEntry = expectedEntries.nextElement();
      ZipEntry actualEntry = actual.getEntry( expectedEntry.getName() );
      compare( expectedEntry, actualEntry );
      testedActualEntries.add( actualEntry.getName() );
    }

    //Backward
    Enumeration<? extends ZipEntry> actualEntries = actual.entries();
    while ( actualEntries.hasMoreElements() ) {
      ZipEntry actualEntry = actualEntries.nextElement();
      if ( testedActualEntries.contains( actualEntry.getName() ) ) {
        continue;
      }
      throw new AssertionFailedError( "Uups. Unexpected element in actual: " + actualEntry.getName() );
    }
    Assert.assertEquals( expected.size(), actual.size() );
  }

  public static void compare( @Nullable ZipEntry expectedEntry, @Nullable ZipEntry actualEntry ) {
    if ( expectedEntry == actualEntry ) {
      return;
    }
    if ( expectedEntry == null ) {
      throw new AssertionFailedError( "ExpectedEntry is null for " + actualEntry.getName() );
    }
    if ( actualEntry == null ) {
      throw new AssertionFailedError( "ActualEntry is null for " + expectedEntry.getName() );
    }

    Assert.assertEquals( expectedEntry.getName(), actualEntry.getName() );
    Assert.assertEquals( expectedEntry.getCrc(), actualEntry.getCrc() );
    Assert.assertEquals( expectedEntry.getSize(), actualEntry.getSize() );
    Assert.assertEquals( "Different Name. Expected: <" + expectedEntry.getName() + "> but was <" + actualEntry.getName() + ">", expectedEntry.getName(), actualEntry.getName() );
    Assert.assertEquals( "Different Size. Expected: <" + expectedEntry.getSize() + "> but was <" + actualEntry.getSize() + ">", expectedEntry.getSize(), actualEntry.getSize() );
    Assert.assertEquals( "Different CRC. Expected: <" + expectedEntry.getCrc() + "> but was <" + actualEntry.getCrc() + ">", expectedEntry.getCrc(), actualEntry.getCrc() );
  }
}
