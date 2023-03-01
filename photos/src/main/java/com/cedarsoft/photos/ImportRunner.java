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
package com.cedarsoft.photos;

import it.neckar.open.crypt.Hash;

import com.cedarsoft.photos.di.Modules;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class ImportRunner {
  public static void main(String[] args) throws IOException {
    Injector injector = Guice.createInjector(Modules.getModules());

    LinkByDateCreator linkByDateCreator = injector.getInstance(LinkByDateCreator.class);

    List<File> failedLinks = new ArrayList<>();

    Importer importer = injector.getInstance(Importer.class);

    ImportReport.Builder importReportBuilder = ImportReport.builder();

    importer.importDirectory(new File("/media/mule/data/media/photos/import/todo/found-somewhere"), new Importer.Listener() {
      @Override
      public void skipped(@Nonnull Hash hash, @Nonnull File fileToImport, @Nonnull File targetFile) {
        importReportBuilder.withAlreadyExisting(hash);

        try {
          File linkPath = linkByDateCreator.createLink(targetFile, hash);
          importReportBuilder.withCreatedLink(linkPath);
        } catch (Exception e) {
          e.printStackTrace();
          failedLinks.add(targetFile);
        }
      }

      @Override
      public void imported(@Nonnull Hash hash, @Nonnull File fileToImport, @Nonnull File targetFile) {
        importReportBuilder.withImportedHash(hash);

        System.out.println("Imported " + fileToImport + " --> " + targetFile.getParentFile().getParentFile().getName() + "/" + targetFile.getParentFile().getName());
        try {
          File link = linkByDateCreator.createLink(targetFile, hash);
          importReportBuilder.withCreatedLink(link);
          System.out.println("\t" + link.getAbsolutePath());
        } catch (Exception e) {
          e.printStackTrace();
          failedLinks.add(targetFile);
        }
      }
    });

    System.out.println("# Import finished ################");
    System.out.println("Failed links:");

    for (File failedLink : failedLinks) {
      System.out.println("\t" + failedLink.getAbsolutePath());
    }


    ImportReport importReport = importReportBuilder.build();

    System.out.println("################################");
    System.out.println("################################");
    System.out.println("##########  RESULT  ############");
    System.out.println("################################");
    System.out.println("################################");
    System.out.println("Imported:       " + importReport.getImportedHashes().size());
    System.out.println("Skipped:        " + importReport.getAlreadyExisting().size());
    System.out.println();
    System.out.println(" Imported Hashes: ");
    for (Hash hash : importReport.getImportedHashes()) {
      System.out.println("\t\t" + hash.getValueAsHex());
    }
    System.out.println();
    System.out.println();
    System.out.println("Created links: ");
    for (File link : importReport.getCreatedLinks()) {
      System.out.println("\t" + link.getAbsolutePath());
    }
  }
}
