package com.cedarsoft.app;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class OfficeLauncher {
  @NonNls
  private static final String OPEN_OFFICE_WINDOWS_2_3 = "c:\\Programme\\Openoffice.org 2.3\\program\\soffice.exe";
  @NonNls
  private static final String OPEN_OFFICE_WINDOWS = "c:\\Programme\\Openoffice.org 2.4\\program\\soffice.exe";
  @NonNls
  private static final String OPEN_OFFICE_LINUX = "/usr/bin/soffice";
  @NonNls
  private static final String EXCEL_WINDOWS = "c:\\Programme\\Microsoft Office\\Office\\excel.exe";

  @NotNull
  @NonNls
  private static final List<String> writerBins = new ArrayList<String>();
  @NotNull
  @NonNls
  private static final List<String> spreadsheetBins = new ArrayList<String>();

  private OfficeLauncher() {
  }

  static {
    writerBins.add( OPEN_OFFICE_LINUX );
    writerBins.add( OPEN_OFFICE_WINDOWS );
    writerBins.add( OPEN_OFFICE_WINDOWS_2_3 );

    spreadsheetBins.add( OPEN_OFFICE_LINUX );
    spreadsheetBins.add( OPEN_OFFICE_WINDOWS );
    spreadsheetBins.add( OPEN_OFFICE_WINDOWS_2_3 );
    spreadsheetBins.add( EXCEL_WINDOWS );
  }

  @NotNull
  public static Process openWriter( @NotNull File file ) throws IOException {
    return openFile( writerBins, file );
  }

  @NotNull
  public static Process openSpreadsheet( @NotNull File file ) throws IOException {
    return openFile( spreadsheetBins, file );
  }

  @NotNull
  private static Process openFile( @NotNull @NonNls
  List<? extends String> possibleBins, @NotNull File file ) throws IOException {
    for ( String possibleBin : possibleBins ) {
      if ( new File( possibleBin ).exists() ) {
        return Runtime.getRuntime().exec( new String[]{possibleBin, file.getAbsolutePath()} );
      }
    }
    throw new IllegalStateException( "No Office installation found..." );
  }
}
