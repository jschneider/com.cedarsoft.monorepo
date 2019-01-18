package com.cedarsoft.commons.javafx;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

/**
 * Helper class for bit set util related stuff
 */
public class BitSetUtils {
  /**
   * Converts a bool array to a bit set
   */
  @Nonnull
  public static BitSet toBitSet(@Nonnull boolean[] booleans) {
    BitSet bitSet = new BitSet();

    for (int i = 0; i < booleans.length; i++) {
      boolean beamState = booleans[i];
      bitSet.set(i, beamState);
    }

    return bitSet;
  }

  @Nonnull
  public static List<? extends BitSet> toBitSets(@Nonnull List<boolean[]> booleans) {
    return booleans.stream()
             .map(BitSetUtils::toBitSet)
             .collect(Collectors.toList());
  }
}
