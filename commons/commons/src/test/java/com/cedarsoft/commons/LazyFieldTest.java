package com.cedarsoft.commons;

import org.junit.*;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class LazyFieldTest {
  @Test
  public void testSimple() throws Exception {
    LazyField<String> field = new LazyField<String>( new LazyField.InstanceFactory<String>() {
      @Nonnull
      @Override
      public String create() {
        return "daString";
      }
    } );

    assertThat( field.getInstanceNullable() ).isNull();
    assertThat( field.getInstance() ).isEqualTo( "daString" );
    assertThat( field.getInstanceNullable() ).isEqualTo( "daString" );
  }
}
