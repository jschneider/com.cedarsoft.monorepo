package com.cedarsoft;

import com.cedarsoft.file.FileType;
import com.cedarsoft.serialization.RegistrySerializer;
import com.cedarsoft.utils.DefaultRegistry;
import com.cedarsoft.utils.StillContainedException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class FileTypeRegistry extends DefaultRegistry<FileType> {
  @NotNull
  public static final FileType LIGHT_ZONE = new FileType( "LightZone", true, new Extension( "_", "lzn.jpg" ) );
  @NotNull
  public static final FileType JPEG = new FileType( "JPEG", false, new Extension( ".", "jpg" ), new Extension( ".", "jpeg" ) );
  @NotNull
  public static final FileType TIFF = new FileType( "TIFF", false, new Extension( ".", "tiff" ), new Extension( ".", "tiff" ) );
  @NotNull
  public static final FileType GIMP = new FileType( "Gimp", false, new Extension( ".", "xcf" ) );
  @NotNull
  public static final FileType PHOTO_SHOP = new FileType( "Photoshop", false, new Extension( ".", "psd" ) );
  @NotNull
  public static final FileType RAW_CANON = new FileType( "Canon Raw", false, new Extension( ".", "cr2" ) );

  @NotNull
  private static final List<FileType> DEFAULT = Arrays.asList( LIGHT_ZONE, JPEG, TIFF, GIMP, RAW_CANON, PHOTO_SHOP );

  @Deprecated
  @TestOnly
  public FileTypeRegistry() {
    this( true );
  }

  @Deprecated
  @TestOnly
  public FileTypeRegistry(boolean registerDefaultTypes) {
    if ( registerDefaultTypes ) {
      ensureDefaultTypesRegistered();
    }
  }

  public FileTypeRegistry( @NotNull Collection<? extends FileType> storedObjects, @Nullable Comparator<FileType> fileTypeComparator ) throws StillContainedException {
    super( storedObjects, fileTypeComparator );
  }

  /**
   * Returns the file types
   *
   * @return the file types
   */
  @NotNull
  public List<? extends FileType> getFileTypes() {
    return getStoredObjects();
  }

  /**
   * Ensures that the default types are registered
   */
  public final void ensureDefaultTypesRegistered() {
    lock.writeLock().lock();
    try {
      if ( !getStoredObjects().isEmpty() ) {
        return;
      }

      //Register the default types
      for ( FileType fileType : DEFAULT ) {
        try {
          store( fileType );
        } catch ( StillContainedException ignore ) {
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @NotNull
  public FileType valueOf( @NotNull @NonNls final String id ) {
    return findStoredObject( new Matcher<FileType>() {
      @Override
      public boolean matches( @NotNull FileType object ) {
        return object.getId().equals( id );
      }
    }, "No FileType found for <" + id + '>' );
  }

  @NotNull
  public FileType get( @NotNull final FileName fileName ) {
    return findStoredObject( new Matcher<FileType>() {
      @Override
      public boolean matches( @NotNull FileType object ) {
        return object.matches( fileName );
      }
    }, "No FileType found for file <" + fileName + '>' );
  }

  @NotNull
  public FileType get( @NotNull @NonNls final String fileName ) {
    return findStoredObject( new Matcher<FileType>() {
      @Override
      public boolean matches( @NotNull FileType object ) {
        return object.matches( fileName );
      }
    }, "No FileType found for file <" + fileName + '>' );
  }

  /**
   * Parses a file name
   * @param fileName the file name to parse
   * @return the file name
   */
  @NotNull
  public FileName parseFileName( @NotNull @NonNls String fileName ) {
    FileType type = get( fileName );
    return type.getFileName( fileName );
  }

  public static class Factory implements RegistrySerializer.RegistryFactory<FileType, FileTypeRegistry> {
    @NotNull
    @Override
    public FileTypeRegistry createRegistry( @NotNull List<? extends FileType> objects, @NotNull Comparator<FileType> comparator ) {
      return new FileTypeRegistry( objects, comparator );
    }
  }
}
