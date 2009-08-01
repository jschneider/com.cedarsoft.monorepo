package com.cedarsoft.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 *
 */
public enum CellTypes implements CellType {
  INTEGER {
    @NotNull
    @Override
    public Integer getValue( @NotNull HSSFCell cell ) {
      return ( int ) cell.getNumericCellValue();
    }},

  STRING {
    @NotNull
    @Override
    public String getValue( @NotNull HSSFCell cell ) throws NoValueFoundException {
      try {
        String value = cell.getRichStringCellValue().getString();
        if ( value == null ) {
          throw new NoValueFoundException( "No value found at: " + getCellLocationString( cell ) );
        }
        return value;
      } catch ( NumberFormatException ignore ) {
        return String.valueOf( ( int ) cell.getNumericCellValue() );
      }
    }},

  BOOLEAN {
    @NotNull
    @Override
    public Boolean getValue( @NotNull HSSFCell cell ) throws NoValueFoundException {
      try {
        return cell.getBooleanCellValue();
      } catch ( NumberFormatException ignore ) {
      }

      try {
        double numValue = cell.getNumericCellValue();
        //noinspection FloatingPointEquality
        return numValue == 1;
      } catch ( NumberFormatException ignore ) {
      }

      String asText = cell.getRichStringCellValue().getString();
      if ( asText.equalsIgnoreCase( "ja" ) ) {
        return true;
      }
      if ( asText.equalsIgnoreCase( "yes" ) ) {
        return true;
      }
      if ( asText.equalsIgnoreCase( "x" ) ) {
        return true;
      }
      if ( asText.equalsIgnoreCase( "1" ) ) {
        return true;
      }

      return false;
    }},

  DATE {
    @Override
    @NotNull
    public Date getValue( @NotNull HSSFCell cell ) throws NoValueFoundException {
      Date value = cell.getDateCellValue();
      if ( value == null ) {
        throw new NoValueFoundException( "No value found at: " + getCellLocationString( cell ) );
      }
      return value;
    }},

  DOUBLE {
    @Override
    @NotNull
    public Double getValue( @NotNull HSSFCell cell ) throws NoValueFoundException {
      return cell.getNumericCellValue();
    }};

  public static String getCellLocationString( @NotNull HSSFCell cell ) {
    return String.valueOf( cell.getCellNum() );
  }
}
