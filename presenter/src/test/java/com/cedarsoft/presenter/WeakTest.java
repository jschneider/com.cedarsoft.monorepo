package com.cedarsoft.presenter;

import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import java.lang.ref.WeakReference;

import static org.testng.Assert.*;

/**
 *
 */
public class WeakTest {

  @Test
  public void testIt() throws InterruptedException {
    DefaultNode root = new DefaultNode( "root" );
    root.addChild( new DefaultNode( "child" ) );

    WeakReference<MyPresenter> presenterReference = new WeakReference<MyPresenter>( new MyPresenter() );
    assertEquals( "thePresentation", presenterReference.get().present( root ) );

    System.gc();
    assertNull( presenterReference.get() );

    if ( presenterReference.get() != null ) {
      System.out.println( "Reference found!" );
      Thread.sleep( 500000 );
    }
  }

  public static class MyPresenter extends AbstractPresenter<String> {
    @Override
    protected boolean shallAddChildren() {
      return true;
    }

    @Override
    protected void bind( @NotNull String presentation, @NotNull StructPart struct, @NotNull Lookup lookup ) {
    }

    @Override
    protected boolean addChildPresentation( @NotNull String presentation, @NotNull StructPart child, int index ) {
      return true;
    }

    @Override
    protected void removeChildPresentation( @NotNull String presentation, @NotNull StructPart child, int index ) {
    }

    @Override
    @NotNull
    protected String createPresentation() {
      return "thePresentation";
    }
  }

}
