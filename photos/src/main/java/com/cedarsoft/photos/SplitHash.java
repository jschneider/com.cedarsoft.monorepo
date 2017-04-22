package com.cedarsoft.photos;

import com.cedarsoft.crypt.Algorithm;
import com.cedarsoft.crypt.Hash;

import javax.annotation.Nonnull;

/**
 * Represents a split hash
 */
public class SplitHash {
  public static final int FIRST_PART_LENGTH = 2;

  @Nonnull
  private final String firstPart;
  @Nonnull
  private final String leftover;

  public SplitHash(@Nonnull String firstPart, @Nonnull String leftover) {
    this.firstPart = firstPart;
    this.leftover = leftover;
  }

  @Nonnull
  public String getFirstPart() {
    return firstPart;
  }

  @Nonnull
  public String getLeftover() {
    return leftover;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    SplitHash splitHash = (SplitHash) obj;

    if (!firstPart.equals(splitHash.firstPart)) {
      return false;
    }
    return leftover.equals(splitHash.leftover);

  }

  @Override
  public int hashCode() {
    int result = firstPart.hashCode();
    result = 31 * result + leftover.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "SplitHash{" + firstPart + ':' + leftover + '}';
  }

  @Nonnull
  public static SplitHash split(@Nonnull Hash hash) {
    return split(hash.getValueAsHex());
  }

  @Nonnull
  public static SplitHash split(@Nonnull String hash) {
    if (hash.length() <= FIRST_PART_LENGTH) {
      throw new IllegalArgumentException("Id <" + hash + "> too short. Min length is 3");
    }

    return new SplitHash(hash.substring(0, FIRST_PART_LENGTH), hash.substring(FIRST_PART_LENGTH));
  }

  @Nonnull
  public Hash toHash(@Nonnull Algorithm algorithm) {
    return Hash.fromHex(algorithm, getHashAsHex());
  }

  /**
   * Returns the complete hash (first part + leftover)
   */
  @Nonnull
  public String getHashAsHex() {
    return firstPart + leftover;
  }
}
