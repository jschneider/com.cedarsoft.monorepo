package com.cedarsoft.commons.struct;

import com.cedarsoft.CanceledException;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 *
 */
public class DepthFirstStructureTreeWalker implements StructureTreeWalker {
  private final Stack<StructPart> queue = new Stack<StructPart>();

  public void walk( @NotNull StructPart root, @NotNull WalkerCallBack walkerCallBack ) {
    queue.add( root );

    while ( !queue.isEmpty() ) {
      StructPart actual = queue.pop();
      try {
        walkerCallBack.nodeReached( actual, Path.calculateLevel( root, actual ) );

        //add the children reversed
        for ( int i = actual.getChildren().size() - 1; i >= 0; i-- ) {
          StructPart child = actual.getChildren().get( i );
          queue.add( child );
        }
      } catch ( CanceledException ignore ) {
        //If canceled the children are *not* added
      }
    }
  }
}
