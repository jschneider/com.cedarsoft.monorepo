package com.cedarsoft.serialization.stax;

import com.cedarsoft.serialization.SerializingStrategy;
import org.codehaus.staxmate.out.SMOutputElement;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @param <T> the type this strategy serializes
 * @param <C> the type of the context
 */
public interface StaxMateSerializingStrategy<T, C> extends SerializingStrategy<T, C, SMOutputElement, XMLStreamReader, XMLStreamException> {
}