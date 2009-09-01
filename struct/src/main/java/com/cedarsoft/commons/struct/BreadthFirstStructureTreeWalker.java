package com.cedarsoft.commons.struct;

import com.cedarsoft.CanceledException;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 */
public class BreadthFirstStructureTreeWalker implements StructureTreeWalker {
  private final Queue<StructPart> queue = new LinkedList<StructPart>();

  public void walk( @NotNull StructPart root, @NotNull WalkerCallBack walkerCallBack ) {
    queue.add( root );

    while ( !queue.isEmpty() ) {
      StructPart actual = queue.poll();

      try {
        walkerCallBack.nodeReached( actual, Path.calculateLevel( root, actual ) );

        //Add the children
        queue.addAll( actual.getChildren() );
      } catch ( CanceledException ignore ) {
        //Ignore the children
      }
    }
  }
}
