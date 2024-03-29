/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package it.neckar.open.zip;

import static it.neckar.open.test.utils.matchers.ContainsOnlyFilesMatcher.containsOnlyFiles;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import it.neckar.open.test.utils.TemporaryFolder;
import it.neckar.open.test.utils.WithTempFiles;

/**
 *
 */
@WithTempFiles
public class ZipCreatorTest {
  @Test
  public void testIt(@Nonnull TemporaryFolder tmp) throws IOException {
    File target = tmp.newFolder( "target" );

    ZipExtractor extractor = new ZipExtractor();
    extractor.extract(target, FileUtils.openInputStream(createZip(tmp)));

    assertEquals( 2, target.listFiles().length );
    Assertions.assertThat(target).matches(containsOnlyFiles("file1", "subDir/file2"));
  }

  @Test
  public void testCondition(@Nonnull TemporaryFolder tmp) throws IOException {
    File target = tmp.newFolder( "target" );

    ZipExtractor extractor = new ZipExtractor( new ZipExtractor.InvertedCondition( new ZipExtractor.Condition() {
      @Override
      public boolean shallExtract( @Nonnull ArchiveEntry zipEntry ) {
        return true;
      }
    } ) );
    extractor.extract(target, FileUtils.openInputStream(createZip(tmp)));
    assertEquals( 0, target.listFiles().length );
  }

  @Nonnull
  private File createZip(@Nonnull TemporaryFolder tmp) throws IOException {
    File file = tmp.newFile( "file.zip" );
    ZipCreator creator = new ZipCreator( file );

    File baseDir1 = tmp.newFolder( "base1" );
    File file1 = new File( baseDir1, "file1" );
    FileUtils.writeStringToFile(file1, "file1content", StandardCharsets.UTF_8);

    File baseDir2 = tmp.newFolder( "base2" );
    File file2 = new File( new File( baseDir2, "subDir" ), "file2" );
    FileUtils.writeStringToFile(file2, "file2content", StandardCharsets.UTF_8);

    assertSame( file, creator.getZipFile() );
    File zip = creator.zip( baseDir1, baseDir2 );
    return zip;
  }
}
