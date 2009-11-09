package com.cedarsoft.presenter;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import static org.testng.Assert.*;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;

/**
 *
 */
public class WeakTest  {

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
    @java.lang.Override
    protected boolean shallAddChildren() {
      return true;
    }

    @java.lang.Override
    protected void bind( @NotNull String presentation, @NotNull StructPart struct, @NotNull Lookup lookup ) {
    }

    @java.lang.Override
    protected boolean addChildPresentation( @NotNull String presentation, @NotNull StructPart child, int index ) {
      return true;
    }

    @java.lang.Override
    protected void removeChildPresentation( @NotNull String presentation, @NotNull StructPart child, int index ) {
    }

    @Override
    @NotNull
    protected String createPresentation() {
      return "thePresentation";
    }
  }

}
