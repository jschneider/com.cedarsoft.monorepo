package com.cedarsoft.serialization.stax;

import com.cedarsoft.serialization.SerializingStrategy;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.staxmate.out.SMOutputElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

/**
 * @param <T> the type this strategy serializes
 */
public interface StaxMateSerializingStrategy<T> extends SerializingStrategy<T, SMOutputElement, XMLStreamReader2> {
}