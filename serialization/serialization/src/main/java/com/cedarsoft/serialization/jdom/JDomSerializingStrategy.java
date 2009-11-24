package com.cedarsoft.serialization.jdom;

import com.cedarsoft.serialization.SerializingStrategy;
import org.jdom.Element;

import java.io.IOException;

/**
 * @param <T> the type
 * @param <C> the type of the context
 */
public interface JDomSerializingStrategy<T, C> extends SerializingStrategy<T, C, Element, Element, IOException> {
}