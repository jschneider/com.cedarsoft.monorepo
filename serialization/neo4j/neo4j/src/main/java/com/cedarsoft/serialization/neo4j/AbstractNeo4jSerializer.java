/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */
package com.cedarsoft.serialization.neo4j;

import com.cedarsoft.serialization.AbstractSerializer;
import com.cedarsoft.serialization.SerializationException;
import com.cedarsoft.version.Version;
import com.cedarsoft.version.VersionException;
import com.cedarsoft.version.VersionRange;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for neo4j serializers
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public abstract class AbstractNeo4jSerializer<T> extends AbstractSerializer<T, Node, Node, IOException, Node, Node> {
  /**
   * This property contains the format version for a node
   */
  @Nonnull
  public static final String PROPERTY_FORMAT_VERSION = "formatVersion";
  /**
   * Property used to identify the order
   */
  @Nonnull
  private static final String PROPERTY_ORDER_INDEX = "orderIndex";

  @Nonnull
  private final String type; //$NON-NLS-1$

  protected AbstractNeo4jSerializer( @Nonnull String type, @Nonnull VersionRange formatVersionRange ) {
    super( formatVersionRange );
    this.type = type;
  }

  @Override
  public final void serialize( @Nonnull T object, @Nonnull Node out ) throws IOException {
    serialize( out, object, getFormatVersion() );
  }

  @Override
  public final void serialize( @Nonnull Node serializeTo, @Nonnull T object, @Nonnull Version formatVersion ) throws VersionException, IOException {
    verifyVersionWritable( formatVersion );

    serializeTo.addLabel(getTypeLabel());
    addVersion( serializeTo );

    serializeInternal( serializeTo, object, formatVersion );
  }

  /**
   * Adds the type and version
   * @param serializeTo the node the type and version is added to
   */
  protected void addVersion( @Nonnull Node serializeTo ) {
    serializeTo.setProperty( PROPERTY_FORMAT_VERSION, getFormatVersion().toString() );
  }

  /**
   * This method must be implemented by sub classes. Serialize the custom fields when necessary.<br>
   * This method is called from serialize(Node, Object, Version). The type label and format version have already been added to the node
   * @param serializeTo the node to serialize to
   * @param object the object
   * @param formatVersion the format version
   * @throws IOException if there is an io problem
   */
  protected abstract void serializeInternal( @Nonnull Node serializeTo, @Nonnull T object, @Nonnull Version formatVersion ) throws IOException;

  /**
   * Returns the type label
   * @return the type label
   */
  @Nonnull
  public Label getTypeLabel() {
    return Label.label(type);
  }

  @Nonnull
  @Override
  public T deserialize( @Nonnull Node in ) throws IOException, VersionException {
    verifyType(in);

    Version version = readVersion( in );
    verifyVersionReadable(version);

    return deserialize(in, version);
  }

  @Nonnull
  protected Version readVersion( @Nonnull Node in ) {
    return Version.parse( ( String ) in.getProperty( PROPERTY_FORMAT_VERSION ) );
  }

  private void verifyType(@Nonnull Node in) throws SerializationException {
    if (!in.hasLabel(getTypeLabel())) {
      throw new SerializationException( SerializationException.Details.INVALID_TYPE, getTypeLabel(), in.getLabels() );
    }
  }

  public <A> void serializeWithRelationships(@Nonnull Iterable<? extends A> objects, @Nonnull Class<A> type, @Nonnull Node node, @Nonnull RelationshipType relationshipType, @Nonnull Version formatVersion ) throws IOException {
    int index = 0;
    for ( A object : objects ) {
      serializeWithRelationship(object, type, node, relationshipType, formatVersion, index);
      index++;
    }
  }

  /**
   * Serializes the given object using a relation
   *
   * @param object           the object that is serialized
   * @param type             the type
   * @param node             the (current) node that is the start for the relationship
   * @param relationshipType the type of the relationship
   * @param formatVersion    the format version
   * @param <A>              the type
   * @throws IOException if there is an io problem
   */
  public <A> void serializeWithRelationship(@Nonnull A object, @Nonnull Class<A> type, @Nonnull Node node, @Nonnull RelationshipType relationshipType, @Nonnull Version formatVersion) throws IOException {
    serializeWithRelationship(object, type, node, relationshipType, formatVersion, null);
  }

  /**
   * Serializes with relationship. Adds an optional index
   */
  protected <A> void serializeWithRelationship(@Nonnull A object, @Nonnull Class<A> type, @Nonnull Node node, @Nonnull RelationshipType relationshipType, @Nonnull Version formatVersion, @Nullable Integer index) throws IOException {
    Node targetNode = node.getGraphDatabase().createNode();
    Relationship relationship = node.createRelationshipTo(targetNode, relationshipType);

    //Add index to ensure order
    if (index != null) {
      relationship.setProperty(PROPERTY_ORDER_INDEX, index);
    }

    serialize(object, type, targetNode, formatVersion);
  }

  @Nonnull
  public <A> A deserializeWithRelationship(@Nonnull Class<A> type, @Nonnull RelationshipType relationshipType, @Nonnull Node node, @Nonnull Version formatVersion ) throws IOException {
    @Nullable Relationship relationship = node.getSingleRelationship( relationshipType, Direction.OUTGOING );
    assert relationship != null;
    return deserialize( type, formatVersion, relationship.getEndNode() );
  }

  @Nonnull
  public <A> List<? extends A> deserializeWithRelationships(@Nonnull Class<A> type, @Nonnull RelationshipType relationshipType, @Nonnull Node node, @Nonnull Version formatVersion ) throws IOException {
    List<A> deserializedList = new ArrayList<>();
    Map<A, Integer> indices = new HashMap<>();

    for ( Relationship relationship : node.getRelationships( relationshipType, Direction.OUTGOING ) ) {
      Node endNode = relationship.getEndNode();
      A deserialized = deserialize(type, formatVersion, endNode);
      deserializedList.add(deserialized);

      @Nullable Integer index = (Integer) relationship.getProperty(PROPERTY_ORDER_INDEX);
      if (index != null) {
        indices.put(deserialized, index);
      }
    }

    if (deserializedList.size() > 1 && !indices.isEmpty()) {
      Collections.sort(deserializedList, new Comparator<A>() {
        @Override
        public int compare(A o1, A o2) {
          @Nullable Integer index1 = indices.get(o1);
          @Nullable Integer index2 = indices.get(o2);

          if (index1 == null) {
            throw new IllegalArgumentException("No index found for <" + o1 + ">");
          }
          if (index2 == null) {
            throw new IllegalArgumentException("No index found for <" + o2 + ">");
          }

          return index1.compareTo(index2);
        }
      });
    }

    return deserializedList;
  }
}
