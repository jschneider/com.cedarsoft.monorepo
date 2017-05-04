package com.cedarsoft.photos;

import com.cedarsoft.crypt.Hash;
import com.cedarsoft.photos.di.Modules;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
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
