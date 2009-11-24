package com.cedarsoft.serialization.stax;

import com.cedarsoft.serialization.SerializingStrategy;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * @param <T> the type this strategy serializes
 * @param <C> the type of the context
 */
public interface StaxSerializingStrategy<T, C> extends SerializingStrategy<T, C, XMLStreamWriter, XMLStreamReader, XMLStreamException> {
}