package com.cedarsoft.utils.tags;

import org.jetbrains.annotations.NotNull;

import java.lang.Override;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class TagFormat {
  private TagFormat() {
  }

  @NotNull
  public static TagFormatter getSimple() {
    return new TagFormatter() {
      @Override
      @NotNull
      public String formatTags( @NotNull List<? extends Tag> tags ) {
        StringBuilder builder = new StringBuilder();
        for ( Iterator<? extends Tag> it = tags.iterator(); it.hasNext(); ) {
          Tag tag = it.next();
          builder.append( tag.getDescription() );

          if ( it.hasNext() ) {
            builder.append( ", " );
          }
        }

        return builder.toString();
      }

      @Override
      @NotNull
      public String format( @NotNull Tagged tagged ) {
        return formatTags( tagged.getTags() );
      }
    };
  }
}
