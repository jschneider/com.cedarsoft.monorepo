package com.cedarsoft.lookup.binding;

import com.cedarsoft.lookup.LookupChangeEvent;
import com.cedarsoft.lookup.Lookups;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: Jun 1, 2007<br>
 * Time: 1:30:20 PM<br>
 */
public class ReflectionListenerTest {
  @Test
  public void testPropertyCallback() {
    MyObject object = new MyObject();

    PropertyCallback<String> callback = new PropertyCallback<String>( object, "name", String.class );
    callback.lookupChanged( new LookupChangeEvent<String>( Lookups.emtyLookup(), String.class, null, "newValue" ) );
    assertEquals( "newValue", object.getName() );
  }

  @Test
  public void testIt() throws NoSuchMethodException {
    MyObject object = new MyObject();
    Method method = object.getClass().getMethod( "setName", String.class );

    ReflectionCallback<String> callback = new ReflectionCallback<String>( object, method );
    callback.lookupChanged( new LookupChangeEvent<String>( Lookups.emtyLookup(), String.class, null, "newValue" ) );
    assertEquals( "newValue", object.getName() );
  }

  public static class MyObject {
    private String name;

    public String getName() {
      return name;
    }

    public void setName( String name ) {
      this.name = name;
    }
  }
}
