package com.cedarsoft.test.utils.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
class AndMatcher<T> extends BaseMatcher<T> {
  private final List<Matcher<?>> matchers;

  public AndMatcher(List<? extends Matcher<?>> matchers) {
    this.matchers = new ArrayList<Matcher<?>>(matchers);
  }

  @Override
  public boolean matches(Object argument) {
    for (Matcher<?> matcher : matchers) {
      if (!matcher.matches(argument)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void describeTo( Description description ) {
    description.appendText( "and(" );
    for ( Iterator<Matcher<?>> it = matchers.iterator(); it.hasNext(); ) {
      it.next().describeTo( description );
      if ( it.hasNext() ) {
        description.appendText( ", " );
      }
    }
    description.appendText( ")" );
  }
}
