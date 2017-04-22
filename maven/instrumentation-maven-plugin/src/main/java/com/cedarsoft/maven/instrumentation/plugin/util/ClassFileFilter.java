package com.cedarsoft.maven.instrumentation.plugin.util;

import org.apache.commons.io.filefilter.FileFilterUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileFilter;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class ClassFileFilter implements FileFilter {
  @Nonnull
  private final FileFilter suffixFilter = FileFilterUtils.suffixFileFilter( ".class" );
  @Nonnull
  private final FileFilter fileFilter = FileFilterUtils.fileFileFilter();

  @Override
  public boolean accept( final File pathname ) {
    return pathname.exists() && suffixFilter.accept( pathname )
      && fileFilter.accept( pathname );
  }

}
