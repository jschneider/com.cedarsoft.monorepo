package com.cedarsoft.serialization;

import com.cedarsoft.Version;
import com.cedarsoft.VersionRange;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @param <T> the type
 * @param <C> the type of the context
 * @param <S> the object to serialize to
 * @param <D> the object to deserialize from
 * @param <E> the exception that might be thrown
 */
public abstract class AbstractSerializer<T, C, S, D, E extends Throwable> implements PluggableSerializer<T, C, S, D, E> {
  @NotNull
  @NonNls
  private final String defaultElementName;

  @NotNull
  private final VersionRange formatVersionRange;

  /**
   * Creates a new version range
   *
   * @param defaultElementName the default element name
   * @param formatVersionRange the version range. The max value is used when written.
   */
  protected AbstractSerializer( @NotNull @NonNls String defaultElementName, @NotNull VersionRange formatVersionRange ) {
    this.defaultElementName = defaultElementName;
    this.formatVersionRange = formatVersionRange;
  }

  /**
   * Helper method that can be used to ensure the right format version for each delegate
   *
   * @param delegate              the delegate
   * @param expectedFormatVersion the expected format version
   */
  protected void verifyDelegatingSerializerVersion( @NotNull Serializer<?, ?> delegate, @NotNull Version expectedFormatVersion ) {
    Version actualVersion = delegate.getFormatVersion();
    if ( !actualVersion.equals( expectedFormatVersion ) ) {
      throw new IllegalArgumentException( "Invalid versions. Expected <" + expectedFormatVersion + "> but was <" + actualVersion + ">" );
    }
  }

  @Override
  @NotNull
  public Version getFormatVersion() {
    return formatVersionRange.getMax();
  }

  /**
   * Returns the format version range that is supported for read
   *
   * @return the format version range that is supported
   */
  @NotNull
  public VersionRange getFormatVersionRange() {
    return formatVersionRange;
  }

  /**
   * Returns the default element name
   *
   * @return the default element name
   */
  @NotNull
  @NonNls
  protected String getDefaultElementName() {
    return defaultElementName;
  }

  /**
   * Helper method that serializes to a byte array
   *
   * @param object  the object
   * @param context the context
   * @return the serialized object
   *
   * @throws IOException
   */
  @NotNull
  public byte[] serializeToByteArray( @NotNull T object, @Nullable C context ) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    serialize( object, out, context );
    return out.toByteArray();
  }

  /**
   * Serializes with a null context
   *
   * @param object the object
   * @param out    the output stream
   * @throws IOException
   */
  public void serialize( @NotNull T object, @NotNull OutputStream out ) throws IOException {
    serialize( object, out, null );
  }

  /**
   * Deserializes without a context
   *
   * @param in the input stream
   * @return the deserialized object
   *
   * @throws IOException
   */
  @NotNull
  public T deserialize( @NotNull InputStream in ) throws IOException {
    return deserialize( in, null );
  }
}
