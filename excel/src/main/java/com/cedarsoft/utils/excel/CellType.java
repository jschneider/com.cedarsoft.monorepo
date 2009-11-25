package com.cedarsoft.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface CellType {
  @NotNull
  Object getValue( @NotNull HSSFCell cell ) throws NoValueFoundException;
}