package com.cedarsoft.serialization.generator.intellij.model;

import com.google.common.collect.ImmutableList;
import com.intellij.psi.PsiClass;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class SerializerModel {
  @Nonnull
  private final ImmutableList<? extends FieldToSerializeEntry> fieldToSerializeEntries;

  @Nonnull
  private final ImmutableList<? extends DelegatingSerializerEntry> delegatingSerializerEntries;

  @Nonnull
  private final PsiClass classToSerialize;

  public SerializerModel( @Nonnull PsiClass classToSerialize, @Nonnull Collection<? extends FieldToSerializeEntry> fieldToSerializeEntries, @Nonnull Collection<? extends DelegatingSerializerEntry> delegatingSerializerEntries ) {
    this.classToSerialize = classToSerialize;
    this.fieldToSerializeEntries = ImmutableList.copyOf( fieldToSerializeEntries );
    this.delegatingSerializerEntries = ImmutableList.copyOf( delegatingSerializerEntries );
  }

  @Nonnull
  public PsiClass getClassToSerialize() {
    return classToSerialize;
  }

  @Nonnull
  public String generateSerializerClassName() {
    return getClassToSerialize().getName() + "Serializer";
  }

  @Nonnull
  public String generateSerializerTestClassName() {
    return getClassToSerialize().getName() + "SerializerTest";
  }

  @Nonnull
  public String generateSerializerVersionTestClassName() {
    return getClassToSerialize().getName() + "SerializerVersionTest";
  }

  @Nonnull
  public ImmutableList<? extends FieldToSerializeEntry> getFieldToSerializeEntries() {
    //noinspection ReturnOfCollectionOrArrayField
    return fieldToSerializeEntries;
  }

  @Nonnull
  public ImmutableList<? extends DelegatingSerializerEntry> getDelegatingSerializerEntries() {
    //noinspection ReturnOfCollectionOrArrayField
    return delegatingSerializerEntries;
  }

  @Nonnull
  public String getClassToSerializeQualifiedName() {
    String qualifiedName = getClassToSerialize().getQualifiedName();
    if ( qualifiedName == null ) {
      throw new IllegalStateException( "No qualified name found for <" + getClassToSerialize() + ">" );
    }

    return qualifiedName;
  }
}
