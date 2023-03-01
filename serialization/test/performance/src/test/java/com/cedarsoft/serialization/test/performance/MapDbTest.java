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
package it.neckar.open.serialization.test.performance;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.annotation.Nonnull;

import org.junit.jupiter.api.*;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import it.neckar.open.test.utils.TemporaryFolder;
import it.neckar.open.test.utils.WithTempFiles;

/**
 */
@WithTempFiles
public class MapDbTest {
  @Test
  public void testIt() throws Exception {
    DB db = DBMaker.memoryDB().make();


    BTreeMap<String, Integer> treeMap = db
      .treeMap("daTreeMap")
      .keySerializer(Serializer.STRING)
      .valueSerializer(Serializer.INTEGER)
      .create();

    treeMap.put( "daKey", 7 );

    assertThat( treeMap ).hasSize( 1 );
    assertThat( treeMap ).containsEntry( "daKey", 7 );
  }

  @Test
  public void testMoreComplex(@Nonnull TemporaryFolder tmp) throws Exception {
    File file = new File(tmp.getRoot(), "db.file");
    assertThat(file).doesNotExist();

    DB db = DBMaker.fileDB(file)
      .closeOnJvmShutdown()
      .transactionEnable()
      .make();

    //a new collection
    ConcurrentNavigableMap<Integer, String> map = db
      .treeMap("collectionName")
      .keySerializer(Serializer.INTEGER)
      .valueSerializer(Serializer.STRING)
      .create();

    map.put( 1, "one" );
    map.put( 2, "two" );

    // map.keySet() is now [1,2]
    assertThat( map ).hasSize( 2 );

    db.commit();  //persist changes into disk

    map.put( 3, "three" );
    assertThat( map ).hasSize( 3 );
    // map.keySet() is now [1,2,3]
    db.rollback(); //revert recent changes
    // map.keySet() is now [1,2]

    assertThat( map ).hasSize( 2 );
    db.close();
  }
}
