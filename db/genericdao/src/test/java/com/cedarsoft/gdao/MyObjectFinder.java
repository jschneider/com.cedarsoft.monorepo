package com.cedarsoft.gdao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
public class MyObjectFinder extends AbstractCollectionFinder<MyObject> {
  @Nullable
  @NonNls
  private String name;

  protected MyObjectFinder() {
    super( MyObject.class );
  }

  @Nullable
  @NonNls
  public String getName() {
    return name;
  }

  public void setName( @NonNls @Nullable String name ) {
    this.name = name;
  }

  @java.lang.Override
  protected void addRestrictions( @NotNull Criteria criteria ) {
    if ( name != null ) {
      criteria.add( Restrictions.eq( "name", name ) );
    }
  }
}
