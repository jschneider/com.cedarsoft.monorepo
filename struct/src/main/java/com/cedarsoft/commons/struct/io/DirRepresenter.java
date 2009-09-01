package com.cedarsoft.commons.struct.io;

import com.cedarsoft.commons.struct.BreadthFirstStructureTreeWalker;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.Path;
import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.commons.struct.StructureTreeWalker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 *
 */
public class DirRepresenter {
  @NotNull
  private final Node root;

  private final boolean rootVisible;

  public DirRepresenter( @NotNull Node root, boolean rootVisible ) {
    this.root = root;
    this.rootVisible = rootVisible;
  }

  /**
   * Stores the struct to the given base dir
   *
   * @param baseDir  the base dir
   * @param callback the callback that is called for each created directory
   */
  public void store( @NotNull final File baseDir, @Nullable final Callback callback ) {
    if ( !baseDir.isDirectory() ) {
      throw new IllegalArgumentException( "Base dir is not a directory" );
    }

    StructureTreeWalker walker = new BreadthFirstStructureTreeWalker();
    walker.walk( root, new StructureTreeWalker.WalkerCallBack() {
      public void nodeReached( @NotNull StructPart node, int level ) {
        Path path = Path.buildPath( node ); //pop the root
        if ( !rootVisible ) {
          path = path.popped();
        }

        File dir = getDir( baseDir, path );
        dir.mkdir();

        if ( callback != null ) {
          callback.dirCreated( node, path, dir );
        }
      }
    } );
  }

  /**
   * Returns the directory for the given base dir and path
   *
   * @param baseDir the base dir
   * @param path    the path
   * @return the dir
   */
  @NotNull
  protected static File getDir( @NotNull File baseDir, @NotNull Path path ) {
    File current = baseDir;
    for ( String element : path.getElements() ) {
      current = new File( current, element );
    }

    return current;
  }


  /**
   * Callback
   */
  public interface Callback {
    /**
     * Is called every time a directory has been created
     *
     * @param node the node
     * @param path the path
     * @param dir  the directory that has been created
     */
    void dirCreated( @NotNull StructPart node, @NotNull Path path, @NotNull File dir );
  }
}
