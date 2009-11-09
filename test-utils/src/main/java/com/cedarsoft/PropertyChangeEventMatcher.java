package com.cedarsoft;

import org.apache.commons.lang.ObjectUtils;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.jetbrains.annotations.Nullable;

import java.beans.PropertyChangeEvent;
import java.lang.Override;

/**
 * Compares property changes
 */
public class PropertyChangeEventMatcher implements IArgumentMatcher {
  @Nullable
  public static PropertyChangeEvent create( @Nullable PropertyChangeEvent event ) {
    EasyMock.reportMatcher( new PropertyChangeEventMatcher( event ) );
    return event;
  }

  @Nullable
  private final PropertyChangeEvent expected;

  public PropertyChangeEventMatcher( @Nullable PropertyChangeEvent expected ) {
    this.expected = expected;
  }

  @Override
  public boolean matches( Object argument ) {
    if ( ObjectUtils.equals( expected, argument ) ) {
      return true;
    }

    if ( expected == null ) {
      return false;
    }

    if ( !( argument instanceof PropertyChangeEvent ) ) {
      return false;
    }

    PropertyChangeEvent actual = ( PropertyChangeEvent ) argument;
    return ObjectUtils.equals( actual.getNewValue(), expected.getNewValue() ) &&
      ObjectUtils.equals( actual.getPropertyName(), expected.getPropertyName() ) &&
      ObjectUtils.equals( actual.getOldValue(), expected.getOldValue() );
  }

  @Override
  public void appendTo( StringBuffer buffer ) {
    buffer.append( "PropertyChangeEvent did not fit: Expected <" + expected + ">" );
  }
}
