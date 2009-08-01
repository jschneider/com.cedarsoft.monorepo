package com.cedarsoft.utils.excel;

import org.jetbrains.annotations.NotNull;
import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 *
 */
public interface CellType {
  @NotNull
  Object getValue( @NotNull HSSFCell cell ) throws NoValueFoundException;
}