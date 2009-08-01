package com.cedarsoft.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.jetbrains.annotations.NotNull;

/**
 * A mapping for a spread sheet column
 */
public class SpreadSheetColumnMapping<T> {
  @NotNull
  private final CellType type;
  private final short index;

  public SpreadSheetColumnMapping( int index, @NotNull CellType type ) {
    this.type = type;
    this.index = ( short ) index;
  }

  public short getIndex() {
    return index;
  }

  @NotNull
  public CellType getType() {
    return type;
  }

  @NotNull
  public T getValue( @NotNull HSSFCell cell ) {
    return ( T ) type.getValue( cell );
  }

  @NotNull
  public T getValue( @NotNull HSSFRow row ) throws NoValueFoundException {
    HSSFCell cell = row.getCell( getIndex() );
    if ( cell == null ) {
      throw new NoValueFoundException( "No cell found at index " + getIndex() + " for row " + row.getRowNum() );
    }
    return getValue( cell );
  }
}
