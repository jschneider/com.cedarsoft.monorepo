package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Formats several tags
 */
public interface TagFormatter {
  /**
   * Formats the given tags
   *
   * @param tags the tags
   * @return the formated tags
   */
  @NotNull
  String formatTags( @NotNull List<? extends Tag> tags );

  @NotNull
  String format( @NotNull Tagged tagged );
}